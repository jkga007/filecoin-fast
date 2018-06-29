package com.filecoin.modules.sys.controller;

import com.filecoin.common.utils.Constant;
import com.filecoin.common.utils.JsonResult;
import com.filecoin.modules.filecoin.entity.DInvitationCodeInfoEntity;
import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.modules.filecoin.service.SysUserExtendService;
import com.filecoin.modules.filecoin.service.WSendEmailService;
import com.filecoin.modules.filecoin.service.WSendMessageService;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import com.filecoin.modules.sys.service.SysUserTokenService;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 注册相关
 * 
 * @author r25437,g20416
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
	public boolean doValidate(
			@PathVariable String type,
			@RequestParam(value = "email",required = false) String email,
			@RequestParam(value = "vcode",required = false) String vcode
	) {
		boolean is = true;
		switch (type){
			case "email":
				if(StringUtils.isNotBlank(email)){
					SysUserEntity sysUserEntity = sysUserService.queryByUserName(email);
					Integer status = sysUserEntity.getStatus();
					if(status == Constant.UserStatus.OK.getValue()){
						is = false;
					}
				}
				break;
			case "vcode":
				if(StringUtils.isNotBlank(vcode)){
					DInvitationCodeInfoEntity dInvitationCodeInfoEntity = dInvitationCodeInfoService.queryObject(vcode);
					if(dInvitationCodeInfoEntity == null){
						is = false;
					}
				}
				break;
			default:
				break;
		}
		return is;
	}


}
