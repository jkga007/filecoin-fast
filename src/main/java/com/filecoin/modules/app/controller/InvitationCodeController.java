package com.filecoin.modules.app.controller;

import com.filecoin.common.utils.JsonResult;
import com.filecoin.modules.app.service.DInvitationCodeInfoService;
import com.filecoin.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author r25437
 * @create 2018-06-25 11:12
 * @desc 邀请码服务
 **/

@RestController
@RequestMapping("/app")
public class InvitationCodeController extends AbstractController {
    @Autowired
    private DInvitationCodeInfoService dInvitationCodeInfoService;
    /*
     *根据邀请码及当前登录用户获取邀请码已经邀请人数量
     */
    @PostMapping("getCountByInvitationCode")
    public JsonResult login(String invitation_code){
        Long userId = getUserId();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("userId",userId);
        int count = dInvitationCodeInfoService.selectCountbyInvitationCode(paramMap);
        return JsonResult.ok().put("count",count);
    }
}
