package com.filecoin.modules.sys.controller;

import com.filecoin.common.utils.Constant;
import com.filecoin.common.utils.JsonResult;
import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.modules.filecoin.service.SysUserExtendService;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册相关,目前未使用任何方法20180630
 *
 * @author r25437, g20416
 * @email support@filecoinon.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
@RequestMapping("regist")
public class SysRegistController extends AbstractController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private DInvitationCodeInfoService dInvitationCodeInfoService;

	@Autowired
	private SysUserExtendService userExtendService;


	/**
	 * 注册jqueryvalidate后端验证
	 * @param type
	 * @return
	 */
	@PostMapping(value = "/doValidate/{type}")
	public String doValidate(
            @PathVariable String type,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "vcode", required = false) String vcode
	) {
		boolean is = true;
        switch (type) {
			case "email":
                if (StringUtils.isNotBlank(email)) {
					SysUserEntity sysUserEntity = sysUserService.queryByUserName(email);
                    if (sysUserEntity != null) {
						Integer status = sysUserEntity.getStatus();
                        if (status == Constant.UserStatus.OK.getValue()) {
							is = false;
						}
					}
				}
				break;
			case "vcode":
                if (StringUtils.isNotBlank(vcode)) {
					DInvitationCodeInfoEntity dInvitationCodeInfoEntity = dInvitationCodeInfoService.queryObject(vcode);
                    if (dInvitationCodeInfoEntity == null) {
						is = false;
                    } else {
                        //查询邀请码邀请人数
                        String invitationCode = dInvitationCodeInfoEntity.getInvitationCode();
                        Map<String, Object> paramMap = new HashMap<>();
                        paramMap.put("invitationCode", invitationCode);
                        int count = dInvitationCodeInfoService.selectCountbyInvitationCode(paramMap);
                        if (count > Constant.INVITATION_MAX_NUM) {
                            is = false;
                        }
                    }
				}
				break;
			default:
				break;
		}
        return is + "";
	}

	/**
	 * 验证所有注册点击按钮后,判断当前用户处于哪个阶段,如果与当前阶段不一致,则跳转到用户处于的阶段
	 * @param type
	 * @param userId
	 * @param step
	 * @return
	 */
	@PostMapping(value = "/doValidateRegist/{type}")
	public JsonResult doValidateRegist(
            @PathVariable String type,
            @RequestParam(value = "userId", required = true) Long userId,
            @RequestParam(value = "step", required = true) String step
	) {

		SysUserEntity user = sysUserService.queryObject(userId);
        if (userId == null) {
			return JsonResult.error("验证注册信息失败!");
		}
        Map<String, Object> map = new HashMap<>();
        map.put("login", Constant.UserStatus.OK.getValue());
        map.put("emailActive", Constant.UserStatus.NEED_ACTIVE.getValue());
        map.put("emailEdit", Constant.UserStatus.NEED_ACTIVE.getValue());
        map.put("mobileActive", Constant.UserStatus.NEED_BIND_MOBILE.getValue());
        map.put("minerInput", Constant.UserStatus.NEED_INPUT_MINER.getValue());

		//前端传递的状态
		Integer getedUserStatus = Integer.parseInt(String.valueOf(map.get(type)));
		//查询当前用户真实状态
		Integer status = user.getStatus();
		//状态不一致
        if (getedUserStatus != status) {
            if (Constant.UserStatus.OK.getValue() == status) {
                return JsonResult.error(4444, "该用户已注册!");
			}
            if (Constant.UserStatus.NEED_ACTIVE.getValue() == status) {
                return JsonResult.error(1, "该用户已注册!处于待激活状态,即将跳转!").put("userId", user.getUserId() + "").put("step", 2);
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
		}

		return JsonResult.ok();
	}


}
