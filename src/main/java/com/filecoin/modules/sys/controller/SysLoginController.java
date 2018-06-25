package com.filecoin.modules.sys.controller;

import com.filecoin.common.exception.FileCoinException;
import com.filecoin.common.utils.Constant;
import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.SnowflakeIdWorker;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.modules.sys.oauth2.UserLoginEntity;
import com.filecoin.modules.sys.service.SysUserTokenService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.filecoin.common.utils.ShiroUtils;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
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
	public Map<String, Object> regist(String email, String vcode, String password, String captcha)throws IOException {
		//本项目已实现，前后端完全分离，但页面还是跟项目放在一起了，所以还是会依赖session
		//如果想把页面单独放到nginx里，实现前后端完全分离，则需要把验证码注释掉(因为不再依赖session了)
		String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		if(!captcha.equalsIgnoreCase(kaptcha)){
			return JsonResult.error("验证码不正确");
		}
		//用户信息
		SysUserEntity user = sysUserService.queryByUserName(email);

		if(user != null){
			return JsonResult.error("该用户已存在!");
		}

		SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);

		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setUserId(idWorker0.nextId());
		userEntity.setPassword(password);
		userEntity.setEmail(email);
		userEntity.setUsername(email);
		userEntity.setInvitationCode(vcode);
        userEntity.setStatus(2);
		userEntity.setCreateUserId(1L);
		List<Long> roleIdList = new ArrayList<>();
		userEntity.setRoleIdList(roleIdList);
		// 发送注册邮件
		sendTemplateMail(userEntity.getEmail(), userEntity.getUserId(), idWorker0.timeGen1());
		sysUserService.save(userEntity);

		//生成token，并保存到数据库
		JsonResult jsonResult = JsonResult.ok("注册成功, 快去激活").put("registEmail",userEntity.getEmail()).put("userId",userEntity.getUserId());


		return jsonResult;
	}

	/**
	 * 激活
	 * @param userId
	 * @param timestamp
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/sys/activation/{userId}/{timestamp}", method = RequestMethod.GET)
	public ModelAndView activation(
			@PathVariable Long userId,
			@PathVariable Long timestamp,
			HttpServletResponse response,
			Model model
	) throws IOException {
		SysUserEntity userEntity = sysUserService.queryObject(userId);
		if(userEntity != null) {
			userEntity.setStatus(1);
			List<Long> roleIdList = new ArrayList<>();
			userEntity.setRoleIdList(roleIdList);
			sysUserService.update(userEntity);
		}
		//生成token，并保存到数据库
		JsonResult jsonResult = sysUserTokenService.createToken(userEntity.getUserId());
		dInvitationCodeInfoService.createInvitationCodeByUser(userEntity.getUserId());
		model.addAttribute("jsonResult",jsonResult);
		return new ModelAndView("sys/regist-result");
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/sys/login", method = RequestMethod.POST)
	public Map<String, Object> login(@RequestBody UserLoginEntity userlogin)throws IOException {
		//本项目已实现，前后端完全分离，但页面还是跟项目放在一起了，所以还是会依赖session
		//如果想把页面单独放到nginx里，实现前后端完全分离，则需要把验证码注释掉(因为不再依赖session了)
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

		//账号锁定
		if(user.getStatus() == 0){
			return JsonResult.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		JsonResult jsonResult = sysUserTokenService.createToken(user.getUserId());
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
	@RequestMapping(value = "/sys/goregist", method = RequestMethod.GET)
	public ModelAndView goregist(Model model) {
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
			context.setVariable("url", "10.110.13.42:8080");
			String emailContent = templateEngine.process("emailTemplate", context);
			helper.setText(emailContent, true);
		} catch (MessagingException e) {
			throw new FileCoinException("邮件发送失败！,请检查您的邮箱！", e);
		}
		javaMailSender.send(message);
	}
	
}
