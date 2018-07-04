package com.filecoin.modules.sys.controller;

import com.filecoin.common.utils.Constant;
import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.SnowflakeIdWorker;
import com.filecoin.modules.filecoin.entity.WPassPhoneModifyEntity;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.modules.filecoin.service.SysUserExtendService;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.oauth2.PassWordModify;
import com.filecoin.modules.sys.service.SysLogService;
import com.filecoin.modules.sys.service.SysUserService;
import com.google.gson.Gson;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 密码修改及找回相关
 *
 * @author r25437, g20416
 * @email support@filecoinon.com
 * @date 2018年7月2日 下午21:00:00
 */
@RestController
@RequestMapping("passControl")
public class SysPassWordController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DInvitationCodeInfoService dInvitationCodeInfoService;

    @Autowired
    private SysUserExtendService userExtendService;
    @Autowired
    private SysLogService logService;

    /**
     * 登录
     */
    @RequestMapping(value = "/editPassWord/{type}", method = RequestMethod.POST)
    public Map<String, Object> editPassWord(
            @PathVariable String type,
            @RequestBody PassWordModify passWordModify) {
        JsonResult jsonResult = null;

        SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);
        SysUserEntity user = null;
        String oldPassWord = passWordModify.getOldPassWord();
        String newPassWord = passWordModify.getNewPassWord();
        String newPassWordConfirm = passWordModify.getNewPassWordConfirm();
        try {
            user = getUser();
            final String salt = user.getSalt();

            //sha256加密
            oldPassWord = new Sha256Hash(oldPassWord, salt).toHex();
            //sha256加密
            newPassWord = new Sha256Hash(newPassWord, salt).toHex();
            //sha256加密
            newPassWordConfirm = new Sha256Hash(newPassWordConfirm, salt).toHex();

            /**
             * 判断状态
             */
            switch (type) {
                case "editPass":
                    //入密码修改待确认表
                    WPassPhoneModifyEntity passPhoneModify = new WPassPhoneModifyEntity();
                    passPhoneModify.setUserId(user.getUserId());
                    passPhoneModify.setInsertTime(new Date());
                    passPhoneModify.setStatus(Constant.PassPhoneModifyStatus.NEED_CONFIRM.getValue());
                    passPhoneModify.setNewValue(newPassWord);
                    passPhoneModify.setOldValue(oldPassWord);
                    passPhoneModify.setType(Constant.PassPhoneModifyTypes.EMAIL_EDIT_PASS.getValue());
                    passPhoneModify.setTimestamp(idWorker0.timeGen1());
                    //发送邮件

                    break;
                case "resendMail":
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
            passWordModify.setOldPassWord(oldPassWord);
            passWordModify.setNewPassWord(newPassWord);
            passWordModify.setNewPassWordConfirm(newPassWordConfirm);

            String params = new Gson().toJson(passWordModify);
            logService.saveLog(getClass().getName() + ".editPassWord", user.getUsername(), params, idWorker0.timeGen1(), "用户修改密码,类型为:" + type);
        }

        return jsonResult;
    }

}
