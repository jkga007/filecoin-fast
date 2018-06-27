package com.filecoin.modules.sys.controller;

import com.filecoin.common.annotation.SysLog;
import com.filecoin.common.exception.FileCoinException;
import com.filecoin.common.utils.Constant;
import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.SnowflakeIdWorker;
import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.modules.sys.oauth2.UserLoginEntity;
import com.filecoin.modules.sys.service.SysUserTokenService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.filecoin.common.utils.ShiroUtils;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
public class SysLoginController extends AbstractController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private DInvitationCodeInfoService dInvitationCodeInfoService;

	@Value("${spring.mail.username}")
	private String sender;

	/**
	 * 验证码
	 */
	@RequestMapping("captcha.jpg")
	public void captcha(HttpServletResponse response)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//生成文字验证码
		String text = producer.createText();
		//生成图片验证码
		BufferedImage image = producer.createImage(text);
		//保存到shiro session
		ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 注册
	 */
	@RequestMapping(value = "/sys/regist", method = RequestMethod.POST)
	public JsonResult regist(String email, String vcode, String password, String captcha, String registType,Long userId)
			throws FileCoinException {
		JsonResult jsonResult = null;
		try{
			String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
			if(!captcha.equalsIgnoreCase(kaptcha)){
				return JsonResult.error("验证码不正确");
			}

			//查询激活码
			DInvitationCodeInfoEntity dInvitationCodeInfoEntity = dInvitationCodeInfoService.queryObject(vcode);
			if(dInvitationCodeInfoEntity == null){
				return JsonResult.error("激活码无效!,请检查!");
			}
			//用户信息
			SysUserEntity user = sysUserService.queryByUserName(email);

			if("U".equals(registType) && userId !=null){
                user = sysUserService.queryObject(userId);
			}


			//查询到用户,同时页面刚刚进来
			if(user != null && "C".equals(registType)){
                Integer status = user.getStatus();
				if(Constant.UserStatus.NEED_ACTIVE.getValue() == status){
					if(user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
						String emailUser = user.getEmail();
						String emailEnd = emailUser.substring(emailUser.indexOf("@")+1,emailUser.length());
						String mailUrl = "mail."+emailEnd;
						return JsonResult.error(1,"该用户已注册!处于待激活状态,请激活!").put("registEmail",user.getEmail()).put("userId",user.getUserId()+"").put("mailUrl",mailUrl);
					}else{
						return JsonResult.error("该用户已激活!,请输入正确密码继续!");
					}

				}
				if(Constant.UserStatus.CLOCK.getValue() == status){
					return JsonResult.error("该用户已锁定!,请联系管理员解锁!");
				}
				if(Constant.UserStatus.OK.getValue() == status){
					return JsonResult.error("该用户已存在!,请登录!");
				}
			}

			SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);

			SysUserEntity userEntity = new SysUserEntity();
            String userOldEmail = "";
            if(user != null){
                userOldEmail = user.getEmail();
            }
			userEntity.setPassword(password);
			userEntity.setEmail(email);
			userEntity.setUsername(email);
			userEntity.setInvitationCode(vcode);
			userEntity.setStatus(Constant.UserStatus.NEED_ACTIVE.getValue());
			userEntity.setCreateUserId(1L);
			List<Long> roleIdList = new ArrayList<>();
			userEntity.setRoleIdList(roleIdList);

			if("C".equals(registType)){
				//新注册
				userEntity.setUserId(idWorker0.nextId());
				sysUserService.save(userEntity);
			}else if("U".equals(registType)){
			    //如果是返回修改
				if(user == null){
					return JsonResult.error("获取用户信息失败!");
				}else{
				    //如果新旧email不一致
				    if(!email.equals(userOldEmail)){
				        //查询新的email是否在数据中存在
                        SysUserEntity userByEmail = sysUserService.queryByUserName(email);
                        //如果新修改的
                        if(userByEmail != null){
                            Long userIdOld = userByEmail.getUserId();
                            //新旧ID不一样
                            if(userIdOld != user.getUserId()){
                                return JsonResult.error("新填写的邮箱已被注册!,请检查!");
                            }
                        }
                    }
                }
				//返回修改的注册
				userEntity.setUserId(user.getUserId());
                userEntity.setSalt(user.getSalt());
				sysUserService.update(userEntity);
			}

			// 发送注册邮件
			sendTemplateMail(userEntity.getEmail(), userEntity.getUserId(), idWorker0.timeGen1());
			String emailUser = userEntity.getEmail();
			String emailEnd = emailUser.substring(emailUser.indexOf("@")+1,emailUser.length());
			String mailUrl = "mail."+emailEnd;

			//生成token，并保存到数据库
			jsonResult = JsonResult.ok("注册成功, 快去激活").put("registEmail",userEntity.getEmail()).put("userId",userEntity.getUserId()+"").put("mailUrl",mailUrl);

		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new FileCoinException("注册失败！,请重试或联系管理员！", e);
		}

		return jsonResult;
	}


	/**
	 * 获取待激活邮件对应的user信息,以便于返回修改
	 * @param userMail
	 * @param userId
	 * @return
	 */
	@PostMapping(value = "/sys/getEditMailUser")
	public JsonResult getEditMailUser(
			@RequestParam(value = "userMail",required = true) String userMail,
			@RequestParam(value = "userId",required = true) Long userId
	) {
		SysUserEntity sysUserEntity = sysUserService.queryObject(userId);
		if(sysUserEntity != null){
			String email = sysUserEntity.getEmail();
			if(!userMail.equals(email)){
				return JsonResult.error("注册用户与邮箱不匹配!,请重新注册!");
			}
			Integer status = sysUserEntity.getStatus();
			//如果不是需要激活的,不需要再发邮件
			if(Constant.UserStatus.NEED_ACTIVE.getValue() != status){
				return JsonResult.error("该邮箱已激活!");
			}
		}else{
			return JsonResult.error("获取原注册信息失败!,请重新注册!");
		}
		return JsonResult.ok("查询成功!").put("retEditMailUser",sysUserEntity);
	}

	/**
	 * 重新发送邮件
	 * @param userMail
	 * @param userId
	 * @return
	 */
	@PostMapping(value = "/sys/resendMail")
	public JsonResult resendMail(
			@RequestParam(value = "userMail",required = true) String userMail,
			@RequestParam(value = "userId",required = true) Long userId
	) {
		SysUserEntity sysUserEntity = sysUserService.queryObject(userId);
		if(sysUserEntity != null){
			String email = sysUserEntity.getEmail();
			if(!userMail.equals(email)){
				return JsonResult.error("注册用户与邮箱不匹配!,请重新注册!");
			}
			Integer status = sysUserEntity.getStatus();
			//如果不是需要激活的,不需要再发邮件
			if(Constant.UserStatus.NEED_ACTIVE.getValue() != status){
				return JsonResult.error("该邮箱已激活!");
			}
		}else{
			return JsonResult.error("注册失败!,请重新注册!");
		}

		SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
		// 发送注册邮件
		sendTemplateMail(userMail, userId, idWorker0.timeGen1());
		return JsonResult.ok("邮件发送成功!");
	}

	/**
	 * 激活
	 * @param userId
	 * @param timestamp
	 * @param response
	 * @return
	 * @throws FileCoinException
	 */
	@RequestMapping(value = "/sys/activation/{userId}/{timestamp}", method = RequestMethod.GET)
	public ModelAndView activation(
			@PathVariable Long userId,
			@PathVariable Long timestamp,
			HttpServletResponse response,
			Model model
	) throws FileCoinException {
		try{
			SysUserEntity userEntity = sysUserService.queryObject(userId);
			if(userEntity != null) {

				Integer status = userEntity.getStatus();
				//待激活状态才去激活
				if(Constant.UserStatus.NEED_ACTIVE.getValue() == status) {
					//变更用户状态为正常状态
					userEntity.setStatus(Constant.UserStatus.OK.getValue());
					List<Long> roleIdList = new ArrayList<>();
					userEntity.setRoleIdList(roleIdList);
					//不修改密码
					userEntity.setPassword(null);
					//处理用户激活事件
					JsonResult jsonResult = sysUserService.activationUser(userEntity);
					model.addAttribute("jsonResult",jsonResult);

				}else if(Constant.UserStatus.CLOCK.getValue() == status){
					model.addAttribute("jsonResult",JsonResult.error("该用户已锁定!,请联系管理员解锁!"));
				}else if(Constant.UserStatus.OK.getValue() == status){
					model.addAttribute("jsonResult",JsonResult.error("该用户已激活!,请登录!"));
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			model.addAttribute("jsonResult",JsonResult.error("激活失败!,请重试或联系管理员!"));
		}

		return new ModelAndView("sys/regist-result");
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestBody UserLoginEntity userlogin)
			throws FileCoinException {
		JsonResult jsonResult = null;
		try{
			String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
			if(!userlogin.getCaptcha().equalsIgnoreCase(kaptcha)){
				return JsonResult.error("验证码不正确");
			}

			//用户信息
			SysUserEntity user = sysUserService.queryByUserName(userlogin.getUsername());

			//账号不存在、密码错误
			if(user == null || !user.getPassword().equals(new Sha256Hash(userlogin.getPassword(), user.getSalt()).toHex())) {
				return JsonResult.error("账号或密码不正确");
			}

			Integer status = user.getStatus();
			if(Constant.UserStatus.NEED_ACTIVE.getValue() == status){
				return JsonResult.error(1,"该用户已注册!处于待激活状态,请激活!").put("userId",user.getUserId()+"").put("email",user.getEmail());
			}
			if(Constant.UserStatus.CLOCK.getValue() == status){
				return JsonResult.error("该用户已锁定!,请联系管理员解锁!");
			}

			//生成token，并保存到数据库
			jsonResult = sysUserTokenService.createToken(user.getUserId());
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new FileCoinException("登录失败！,请重试或联系管理员！", e);
		}
		return jsonResult;
	}


	/**
	 * 退出
	 */
	@RequestMapping(value = "/sys/logout", method = RequestMethod.POST)
	public JsonResult logout() {
		sysUserTokenService.logout(getUserId());
		return JsonResult.ok();
	}

	/**
	 * 去首页,ipfs首页
	 */
	@RequestMapping(value = "/sys/goindex", method = RequestMethod.GET)
	public ModelAndView goindex(Model model) {
		model.addAttribute("msg","aaa");
		return new ModelAndView("sys/index-filecoin");
	}

	/**
	 * 去主页,登录后的主页
	 */
	@RequestMapping(value = "/sys/gomasterindex", method = RequestMethod.GET)
	public ModelAndView gomasterindex(Model model) {
		model.addAttribute("msg","aaa");
		return new ModelAndView("sys/index-dashboard");
	}

	/**
	 * 去注册页
	 */
	@RequestMapping(value = "/sys/goregist", method = {RequestMethod.GET})
	public ModelAndView goregist(
			Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/regist-filecoin");
	}
	/**
	 * 去登录页
	 */
	@RequestMapping(value = "/sys/gologin", method = RequestMethod.GET)
	public ModelAndView gologin(Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/login");
	}

	/**
	 * 去矿工管理页
	 */
	@RequestMapping(value = "/sys/gominer", method = RequestMethod.GET)
	public ModelAndView gominer(Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/miner");
	}

	/**
	 * 跳转付款设置
	 */
	@RequestMapping(value = "/sys/gomywallet", method = RequestMethod.GET)
	public ModelAndView gomywallet(Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/my-wallet");
	}

	/**
	 * 跳转安全中心
	 */
	@RequestMapping(value = "/sys/gosecurity", method = RequestMethod.GET)
	public ModelAndView gosecurity(Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/security");
	}

	/**
	 * 跳转个人中心
	 */
	@RequestMapping(value = "/sys/gosettings", method = RequestMethod.GET)
	public ModelAndView gosettings(Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/settings");
	}

	/**
	 * 跳转帮助中心
	 */
	@RequestMapping(value = "/sys/gofaq", method = RequestMethod.GET)
	public ModelAndView gofaq(Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/faq");
	}

	/**
	 * 跳转推广有礼
	 */
	@RequestMapping(value = "/sys/gohighway", method = RequestMethod.GET)
	public ModelAndView gohighway(Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/highway");
	}

	/**
	 * 去付款记录页
	 */
	@RequestMapping(value = "/sys/gopay", method = RequestMethod.GET)
	public ModelAndView gopay(Model model) {
		model.addAttribute("msg","bbb");
		return new ModelAndView("sys/pay");
	}

	/**
	 * 测试发送简单邮件
	 */
	@RequestMapping(value = "testsimplemail", method = RequestMethod.GET)
	public void sendSimpleEmail() {

		SimpleMailMessage message = new SimpleMailMessage();
		// 发送者
		message.setFrom(sender);
		// 接收者
		message.setTo("jkgao007@163.com");
		//邮件主题
		message.setSubject("Java资源分享网密码重置邮件111");
		// 邮件内容
		message.setText("测试内容哈哈哈哈哈");
		javaMailSender.send(message);
	}

	public void sendTemplateMail(String recipient,Long userId,Long timestamp) throws FileCoinException {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(recipient);
			helper.setSubject("IPFS星际云矿池");
			Context context = new Context();
			context.setVariable("id", userId);
			context.setVariable("timestamp", timestamp);
			context.setVariable("url", "120.79.242.64");
			context.setVariable("other", "utm_campaign=filecoin-email-verification&utm_content=html&utm_medium=email&utm_source=verification-email");
			String emailContent = templateEngine.process("emailTemplate", context);
			helper.setText(emailContent, true);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new FileCoinException("邮件发送失败！,请检查您的邮箱！", e);
		}
		javaMailSender.send(message);
	}

	/**
	 * 获取交易所实时价格
	 */
	@RequestMapping(value = "/sys/getCoinTickers", method = RequestMethod.POST)
	public JsonResult getCoinTickers(Model model) {
		String s = null;
		try{
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url("https://block.cc/api/v1/coin/tickers?coin=filecoin&exchange=&symbol=&page=0&size=50")
					.get()
					.build();

			Response response = client.newCall(request).execute();
			s = response.body().string();
		}catch(Exception e){
			throw new FileCoinException("获取交易所实时价格错误!");
		}

		return JsonResult.ok("获取交易所实时价格成功").put("coinData",s);
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/loginBack", method = RequestMethod.POST)
	public Map<String, Object> login(String username, String password, String captcha)throws IOException {
		//本项目已实现，前后端完全分离，但页面还是跟项目放在一起了，所以还是会依赖session
		//如果想把页面单独放到nginx里，实现前后端完全分离，则需要把验证码注释掉(因为不再依赖session了)
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return JsonResult.error("验证码不正确");
		}

		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(username);

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex()) && !user.getUsername().equals("admin")) {
			return JsonResult.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return JsonResult.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		JsonResult r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}
	
}
