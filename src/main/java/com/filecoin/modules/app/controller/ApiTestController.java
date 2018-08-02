package com.filecoin.modules.app.controller;


import com.filecoin.common.utils.JsonResult;
import com.filecoin.modules.app.annotation.Login;
import com.filecoin.modules.app.annotation.LoginUser;
import com.filecoin.modules.app.entity.UserEntity;
import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP测试接口
 *
 * @author r25437, g20416
 * @email support@filecoinon.com
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/app")
public class ApiTestController {

    @Autowired
    private DInvitationCodeInfoService dInvitationCodeInfoService;

    /**
     * 获取用户信息
     */
    @Login
    @GetMapping("userInfo")
    public JsonResult userInfo(@LoginUser UserEntity user) {
        return JsonResult.ok().put("user", user);
    }

    @Login
    @GetMapping("/getInvitaCodeByUserId")
    public JsonResult getInvitaCodeByUserId(@RequestAttribute("userId") Long userId) {
        DInvitationCodeInfoEntity dInvitationCodeInfoEntity = dInvitationCodeInfoService.queryObjectByUserId(userId);
        return JsonResult.ok().put("invitationCode", dInvitationCodeInfoEntity.getInvitationCode());
    }

    /**
     * 获取用户ID
     */
    @Login
    @GetMapping("userId")
    public JsonResult userInfo(@RequestAttribute("userId") Long userId) {
        return JsonResult.ok().put("userId", userId);
    }

    /**
     * 忽略Token验证测试
     */
    @GetMapping("notToken")
    public JsonResult notToken() {
        return JsonResult.ok().put("msg", "无需token也能访问。。。");
    }

}
