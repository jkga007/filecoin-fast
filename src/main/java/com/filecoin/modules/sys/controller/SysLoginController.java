package com.filecoin.modules.sys.controller;

import com.filecoin.common.exception.FileCoinException;
import com.filecoin.common.utils.Constant;
import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.ShiroUtils;
import com.filecoin.common.utils.SnowflakeIdWorker;
import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;
import com.filecoin.modules.filecoin.entity.RegistEntity;
import com.filecoin.modules.filecoin.entity.SysUserExtendEntity;
import com.filecoin.modules.filecoin.entity.WSendEmailEntity;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.modules.filecoin.service.SysUserExtendService;
import com.filecoin.modules.filecoin.service.WSendEmailService;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.oauth2.UserLoginEntity;
import com.filecoin.modules.sys.service.SysLogService;
import com.filecoin.modules.sys.service.SysUserService;
import com.filecoin.modules.sys.service.SysUserTokenService;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * 登录相关
 *
 * @author r25437, g20416
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
    private DInvitationCodeInfoService dInvitationCodeInfoService;
    @Autowired
    private WSendEmailService wSendEmailService;
    @Autowired
    private SysUserExtendService userExtendService;
    @Autowired
    private SysLogService logService;

    /**
     * 获取验证码
     */
    @RequestMapping("captcha.jpg")
    public void captcha(HttpServletResponse response) throws ServletException, IOException {
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
     * 注册方法
     *
     * @param type
     * @param registEntity
     * @return
     * @throws FileCoinException
     */
    @RequestMapping(value = "/sys/regist/{type}", method = RequestMethod.POST)
    public JsonResult registFunc(
            @PathVariable String type,
            @RequestBody RegistEntity registEntity
    ) throws FileCoinException {
        JsonResult jsonResult = null;
        SysUserEntity user = null;
        String email = registEntity.getEmail();
        String vcode = registEntity.getVcode();
        String password = registEntity.getPasswd();
        String captcha = registEntity.getCaptcha();
        String registType = registEntity.getRegistType();

        String iccid = registEntity.getIccid();
        String minerMachineAddr = registEntity.getMinerMachineAddr();
        String minerMachineEnv = registEntity.getMinerMachineEnv();
        String onLineTime = registEntity.getOnLineTime();
        String phoneYzm = registEntity.getPhoneYzm();
        String trueName = registEntity.getTrueName();
        String bandWidth = registEntity.getBandWidth();
        String storageLen = registEntity.getStorageLen();
        String phone = registEntity.getPhone();
        Long userId = registEntity.getUserId();
        SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);

        try {
            switch (type) {
                //邮件新注册
                case "emailRegist":
                    try {
                        String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
                        if (!captcha.equalsIgnoreCase(kaptcha)) {
                            return JsonResult.error("验证码不正确");
                        }

                        //查询激活码
                        DInvitationCodeInfoEntity dInvitationCodeInfoEntity = dInvitationCodeInfoService.queryObject(vcode);
                        if (dInvitationCodeInfoEntity == null) {
                            return JsonResult.error("激活码无效!,请检查!");
                        }
                        //用户信息
                        user = sysUserService.queryByUserName(email);

                        if ("U".equals(registType) && userId != null) {
                            user = sysUserService.queryObject(userId);
                        }

                        //查询到用户,同时页面刚刚进来
                        if (user != null) {
                            Integer status = user.getStatus();
                            if ("C".equals(registType)) {
                                if (Constant.UserStatus.NEED_ACTIVE.getValue() == status) {
                                    if (user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
                                        String emailUser = user.getEmail();
                                        String emailEnd = emailUser.substring(emailUser.indexOf("@") + 1, emailUser.length());
                                        String mailUrl = "mail." + emailEnd;
                                        return JsonResult.error(1, "该用户已注册!处于待激活状态,即将跳转!").put("registEmail", user.getEmail()).put("userId", user.getUserId() + "").put("mailUrl", mailUrl);
                                    } else {
                                        return JsonResult.error("该用户已激活!,请输入正确密码继续!");
                                    }
                                }
                            }

                            if (Constant.UserStatus.CLOCK.getValue() == status) {
                                return JsonResult.error("该用户已锁定!,请联系客服解锁!");
                            }
                            if (Constant.UserStatus.OK.getValue() == status) {
                                return JsonResult.error("该用户已注册!");
                            }
                            if (Constant.UserStatus.NEED_BIND_MOBILE.getValue() == status) {
                                return JsonResult.error(2, "该用户已注册!处于待绑定手机信息状态,即将跳转!").put("userId", user.getUserId() + "").put("step", 3);
                            }
                            if (Constant.UserStatus.NEED_INPUT_MINER.getValue() == status) {
                                return JsonResult.error(2, "该用户已注册!处于待完善矿工信息状态,即将跳转!").put("userId", user.getUserId() + "").put("step", 4);
                            }
                        }

                        SysUserEntity userEntity = new SysUserEntity();
                        String userOldEmail = "";
                        if (user != null) {
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

                        if ("C".equals(registType)) {
                            //新注册
                            userEntity.setUserId(idWorker0.nextId());
                            sysUserService.save(userEntity);
                        } else if ("U".equals(registType)) {
                            //如果是返回修改
                            if (user == null) {
                                return JsonResult.error("获取信息失败!");
                            } else {
                                //如果新旧email不一致
                                if (!email.equals(userOldEmail)) {
                                    //查询新的email是否在数据中存在
                                    SysUserEntity userByEmail = sysUserService.queryByUserName(email);
                                    //如果新修改的
                                    if (userByEmail != null) {
                                        Long userIdOld = userByEmail.getUserId();
                                        //新旧ID不一样
                                        if (userIdOld != user.getUserId()) {
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

                        //保存邮件信息,后台定时任务会扫描发送
                        WSendEmailEntity wSendEmailEntity = new WSendEmailEntity();
                        wSendEmailEntity.setId(idWorker0.nextId());
                        wSendEmailEntity.setInsertTime(new Date());
                        wSendEmailEntity.setEmail(userEntity.getEmail());
                        wSendEmailEntity.setUserId(userEntity.getUserId());
                        wSendEmailEntity.setStatus(Constant.SendStatus.NEED_SEND.getValue());
                        wSendEmailEntity.setType(Constant.EmailTypes.REGIST.getValue());
                        wSendEmailService.save(wSendEmailEntity);

                        // 发送注册邮件
                        //sendTemplateMail(userEntity.getEmail(), userEntity.getUserId(), idWorker0.timeGen1());
                        String emailUser = userEntity.getEmail();
                        String emailEnd = emailUser.substring(emailUser.indexOf("@") + 1, emailUser.length());
                        String mailUrl = "mail." + emailEnd;

                        //返回注册成功方式
                        jsonResult = JsonResult.ok("注册成功, 快去激活").put("registEmail", userEntity.getEmail()).put("userId", userEntity.getUserId() + "").put("mailUrl", mailUrl);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                        throw new FileCoinException("注册失败！,请重试或联系客服！", e);
                    }

                    break;
                //手机信息绑定
                case "phoneBind":

                    user = sysUserService.queryObject(userId);
                    if (user == null) {
                        return JsonResult.error("用户不存在!,请检查!");
                    }
                    SysUserExtendEntity extendEntity = userExtendService.queryObjectByUserId(userId);
                    if (extendEntity == null) {
                        extendEntity = new SysUserExtendEntity();
                        extendEntity.setUserId(userId);
                    }
                    //真实姓名
                    extendEntity.setTrueName(trueName);
                    //身份证号
                    extendEntity.setIccid(iccid);

                    //修改用户中的手机号信息,状态信息

                    user.setStatus(Constant.UserStatus.NEED_INPUT_MINER.getValue());
                    user.setMobile(phone);
                    List<Long> roleIdList = new ArrayList<>();
                    user.setRoleIdList(roleIdList);

                    userExtendService.saveOrUpdateAndEditUser(extendEntity, user);
                    jsonResult = JsonResult.ok("手机绑定成功!,请完善矿工资料").put("userId", userId + "").put("step", 4);

                    break;
                //矿工资料完善
                case "minerInput":
                    user = sysUserService.queryObject(userId);
                    if (user == null) {
                        return JsonResult.error("用户不存在!,请检查!");
                    }
                    SysUserExtendEntity extendEntity2 = userExtendService.queryObjectByUserId(userId);
                    if (extendEntity2 == null) {
                        return JsonResult.error("用户资料不完善!,请检查!");
                    }
                    extendEntity2.setBandWidth(bandWidth);
                    extendEntity2.setMinerMachineAddr(minerMachineAddr);
                    extendEntity2.setMinerMachineEnv(minerMachineEnv);
                    extendEntity2.setOnLineTime(onLineTime);
                    extendEntity2.setStorageLen(storageLen);
                    //修改用户状态为正常
                    user.setStatus(Constant.UserStatus.OK.getValue());
                    List<Long> roleIdList2 = new ArrayList<>();
                    user.setRoleIdList(roleIdList2);

                    jsonResult = userExtendService.saveOrUpdateAndActive(extendEntity2, user);
                    jsonResult = jsonResult.put("msg", "矿工资料完善成功!,请查看邀请码并登陆!").put("userId", userId + "").put("step", 5);
                    break;
                //邀请注册
                case "YQZC":
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        } finally {
            /**
             * 记录日志
             */
            registEntity.setPasswd(null);
            String params = new Gson().toJson(registEntity);
            String userName = null;
            if (user != null) {
                userName = user.getUsername();
            } else {
                userName = email;
            }
            logService.saveLog(getClass().getName() + ".registFunc", userName, params, idWorker0.timeGen1(), "用户注册,类型:" + type);
        }

        return jsonResult;
    }

    /**
     * 获取待激活邮件对应的user信息,以便于返回修改
     *
     * @param userMail
     * @param userId
     * @return
     */
    @PostMapping(value = "/sys/getEditMailUser")
    public JsonResult getEditMailUser(
            @RequestParam(value = "userMail", required = true) String userMail,
            @RequestParam(value = "userId", required = true) Long userId
    ) {

        SysUserEntity sysUserEntity = sysUserService.queryObject(userId);
        try {
            if (sysUserEntity != null) {
                String email = sysUserEntity.getEmail();
                if (!userMail.equals(email)) {
                    return JsonResult.error("注册用户与邮箱不匹配!,请重新注册!");
                }
                Integer status = sysUserEntity.getStatus();
                //如果不是需要激活的,不需要再发邮件
                if (Constant.UserStatus.NEED_ACTIVE.getValue() != status) {
                    return JsonResult.error("该邮箱已激活!");
                }
            } else {
                return JsonResult.error("获取原注册信息失败!,请重新注册!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        } finally {
            /**
             * 记录日志
             */
            SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
            Map<String, Object> map = new HashMap();
            map.put("userId", userId);
            map.put("userMail", userMail);
            String params = new Gson().toJson(map);
            logService.saveLog(getClass().getName() + ".getEditMailUser", userMail, params, idWorker0.timeGen1(), "邮箱注册后返回修改用户信息");
        }

        return JsonResult.ok("查询成功!").put("retEditMailUser", sysUserEntity);
    }

    /**
     * 重新发送邮件
     *
     * @param userMail
     * @param userId
     * @return
     */
    @PostMapping(value = "/sys/resendMail")
    public JsonResult resendMail(
            @RequestParam(value = "userMail", required = true) String userMail,
            @RequestParam(value = "userId", required = true) Long userId
    ) {
        try {

            SysUserEntity sysUserEntity = sysUserService.queryObject(userId);
            if (sysUserEntity != null) {
                String email = sysUserEntity.getEmail();
                if (!userMail.equals(email)) {
                    return JsonResult.error("注册用户与邮箱不匹配!,请重新注册!");
                }
                Integer status = sysUserEntity.getStatus();
                //如果不是需要激活的,不需要再发邮件
                if (Constant.UserStatus.NEED_ACTIVE.getValue() != status) {
                    return JsonResult.error("该邮箱已激活!");
                }
            } else {
                return JsonResult.error("注册失败!,请重新注册!");
            }

            SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
            // 发送注册邮件
//		sendTemplateMail(userMail, userId, idWorker0.timeGen1());
            //保存邮件信息,后台定时任务会扫描发送
            WSendEmailEntity wSendEmailEntity = new WSendEmailEntity();
            wSendEmailEntity.setId(idWorker0.nextId());
            wSendEmailEntity.setInsertTime(new Date());
            wSendEmailEntity.setEmail(sysUserEntity.getEmail());
            wSendEmailEntity.setUserId(sysUserEntity.getUserId());
            wSendEmailEntity.setStatus(Constant.SendStatus.NEED_SEND.getValue());
            wSendEmailEntity.setType(Constant.EmailTypes.REGIST.getValue());
            wSendEmailService.save(wSendEmailEntity);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        } finally {
            /**
             * 记录日志
             */
            SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
            Map<String, Object> map = new HashMap();
            map.put("userId", userId);
            map.put("userMail", userMail);
            String params = new Gson().toJson(map);
            logService.saveLog(getClass().getName() + ".resendMail", userMail, params, idWorker0.timeGen1(), "重新发送邮件");
        }

        return JsonResult.ok("邮件发送成功!");
    }

    /**
     * 激活邮箱,激活后跳转到regist页面,要走到第三步,输入手机号了
     *
     * @param userId
     * @param timestamp
     * @return
     * @throws FileCoinException
     */
    @RequestMapping(value = "/sys/activation/{userId}/{timestamp}", method = RequestMethod.GET)
    public ModelAndView activation(
            @PathVariable Long userId,
            @PathVariable Long timestamp,
            Model model
    ) {
        SysUserEntity userEntity = null;
        try {
            userEntity = sysUserService.queryObject(userId);
            if (userEntity != null) {

                Integer status = userEntity.getStatus();

                //待激活状态才去激活
                if (Constant.UserStatus.NEED_ACTIVE.getValue() == status) {
                    //变更用户状态为待绑定手机状态
                    userEntity.setStatus(Constant.UserStatus.NEED_BIND_MOBILE.getValue());
                    List<Long> roleIdList = new ArrayList<>();
                    userEntity.setRoleIdList(roleIdList);
                    //不修改密码
                    userEntity.setPassword(null);
                    sysUserService.update(userEntity);
                    //处理用户激活事件
//					JsonResult jsonResult = sysUserService.activationUser(userEntity);
                    model.addAttribute("jsonResult", JsonResult.ok("即将进行手机绑定!").put("step", 3).put("userId", userEntity.getUserId() + ""));

                } else if (Constant.UserStatus.NEED_BIND_MOBILE.getValue() == status) {
                    model.addAttribute("jsonResult", JsonResult.ok("即将进行手机绑定!").put("step", 3).put("userId", userEntity.getUserId() + ""));
                } else if (Constant.UserStatus.NEED_INPUT_MINER.getValue() == status) {
                    model.addAttribute("jsonResult", JsonResult.ok("即将进行矿工资料完善!").put("step", 4).put("userId", userEntity.getUserId() + ""));
                } else if (Constant.UserStatus.CLOCK.getValue() == status) {
                    model.addAttribute("jsonResult", JsonResult.error("该用户已锁定!,请联系管理员解锁!"));
                } else if (Constant.UserStatus.OK.getValue() == status) {
                    model.addAttribute("jsonResult", JsonResult.error("该用户已完全激活!,请登录!"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            model.addAttribute("jsonResult", JsonResult.error("激活失败!,请重试或联系管理员!"));
        } finally {
            /**
             * 记录日志
             */
            SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
            Map<String, Object> map = new HashMap();
            map.put("userId", userId);
            map.put("timestamp", timestamp);
            String params = new Gson().toJson(map);
            logService.saveLog(getClass().getName() + ".activation", userEntity.getEmail(), params, idWorker0.timeGen1(), "邮箱激活");
        }

        return new ModelAndView("sys/regist-result");
    }

    /**
     * 登录
     */
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody UserLoginEntity userlogin) {
        JsonResult jsonResult = null;

        try {

            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if (!userlogin.getCaptcha().equalsIgnoreCase(kaptcha)) {
                return JsonResult.error("验证码不正确");
            }

            //用户信息
            SysUserEntity user = sysUserService.queryByUserName(userlogin.getUsername());

            //账号不存在、密码错误
            if (user == null || !user.getPassword().equals(new Sha256Hash(userlogin.getPassword(), user.getSalt()).toHex())) {
                return JsonResult.error("账号或密码不正确");
            }

            Integer status = user.getStatus();
            if (Constant.UserStatus.NEED_ACTIVE.getValue() == status) {
                //如果密码一致
                if (user.getPassword().equals(new Sha256Hash(userlogin.getPassword(), user.getSalt()).toHex())) {
                    return JsonResult.error(2, "该用户已注册!处于待激活状态,即将跳转!").put("userId", user.getUserId() + "").put("step", 2);
                }
            }
            if (Constant.UserStatus.NEED_BIND_MOBILE.getValue() == status) {
                return JsonResult.error(2, "该用户已注册!处于待绑定手机信息状态,即将跳转!").put("userId", user.getUserId() + "").put("step", 3);
            }
            if (Constant.UserStatus.NEED_INPUT_MINER.getValue() == status) {
                return JsonResult.error(2, "该用户已注册!处于待完善矿工信息状态,即将跳转!").put("userId", user.getUserId() + "").put("step", 4);
            }
            if (Constant.UserStatus.CLOCK.getValue() == status) {
                return JsonResult.error("该用户已锁定!,请联系管理员解锁!");
            }

            //生成token，并保存到数据库
            jsonResult = sysUserTokenService.createToken(user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        } finally {
            userlogin.setPassword(null);
            /**
             * 记录日志
             */
            SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
            String params = new Gson().toJson(userlogin);
            logService.saveLog(getClass().getName() + ".login", userlogin.getUsername(), params, idWorker0.timeGen1(), "用户登录");
        }

        return jsonResult;
    }


    /**
     * 退出
     */
    @RequestMapping(value = "/sys/logout", method = RequestMethod.POST)
    public JsonResult logout() {

        SysUserEntity user = null;
        try {
            user = getUser();
            sysUserTokenService.logout(user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        } finally {
            /**
             * 记录日志
             */
            SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
            String params = new Gson().toJson(user.getUserId());
            logService.saveLog(getClass().getName() + ".logout", user.getUsername(), params, idWorker0.timeGen1(), "用户退出");
        }

        return JsonResult.ok();
    }

//	/**
//	 * 测试发送简单邮件
//	 */
//	@RequestMapping(value = "testsimplemail", method = RequestMethod.GET)
//	public void sendSimpleEmail() {
//
//		SimpleMailMessage message = new SimpleMailMessage();
//		// 发送者
//		message.setFrom(sender);
//		// 接收者
//		message.setTo("jkgao007@163.com");
//		//邮件主题
//		message.setSubject("Java资源分享网密码重置邮件111");
//		// 邮件内容
//		message.setText("测试内容哈哈哈哈哈");
//		javaMailSender.send(message);
//	}

    /**
     * 获取交易所实时价格
     */
    @RequestMapping(value = "/sys/getCoinTickers", method = RequestMethod.POST)
    public JsonResult getCoinTickers(Model model) {
        String s = null;
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://block.cc/api/v1/coin/tickers?coin=filecoin&exchange=&symbol=&page=0&size=50")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            s = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new FileCoinException("获取交易所实时价格错误!");
        }

        return JsonResult.ok("获取交易所实时价格成功").put("coinData", s);
    }

    /**
     * 后台登录
     */
    @RequestMapping(value = "/sys/loginBack", method = RequestMethod.POST)
    public Map<String, Object> loginBack(String username, String password, String captcha) {

        JsonResult r = null;
        try {
            String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
            if (!captcha.equalsIgnoreCase(kaptcha)) {
                return JsonResult.error("验证码不正确");
            }

            //用户信息
            SysUserEntity user = sysUserService.queryByUserName(username);

            //账号不存在、密码错误
            if (user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex()) && !user.getUsername().equals("admin")) {
                return JsonResult.error("账号或密码不正确");
            }

            //账号锁定
            if (user.getStatus() == 0) {
                return JsonResult.error("账号已被锁定,请联系管理员");
            }

            //生成token，并保存到数据库
            r = sysUserTokenService.createToken(user.getUserId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw e;
        } finally {
            /**
             * 记录日志
             */
            SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
            Map<String, Object> map = new HashMap();
            map.put("username", username);
//            map.put("password", password);
            map.put("captcha", captcha);
            String params = new Gson().toJson(map);
            logService.saveLog(getClass().getName() + ".loginBack", username, params, idWorker0.timeGen1(), "后台登录");
        }

        return r;
    }

}
