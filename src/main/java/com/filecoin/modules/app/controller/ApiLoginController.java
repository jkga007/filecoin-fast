package com.filecoin.modules.app.controller;


import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.JwtUtils;
import com.filecoin.common.validator.Assert;
import com.filecoin.modules.app.service.UserService;
import com.filecoin.modules.sys.oauth2.AppLoginEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * APP登录授权
 *
 * @author r25437, g20416
 * @email support@filecoinon.com
 * @date 2017-03-23 15:31
 */
@RestController
@RequestMapping("/app")
public class ApiLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录
     */
    @PostMapping("login")
    public JsonResult login(@RequestBody AppLoginEntity appLoginEntity) {
        Assert.isBlank(appLoginEntity.getMobile(), "手机号不能为空");
        Assert.isBlank(appLoginEntity.getPassword(), "密码不能为空");

        //用户登录
        long userId = userService.login(appLoginEntity.getMobile(), appLoginEntity.getPassword());

        //生成token
        String token = jwtUtils.generateToken(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());

        return JsonResult.ok(map);
    }

}
