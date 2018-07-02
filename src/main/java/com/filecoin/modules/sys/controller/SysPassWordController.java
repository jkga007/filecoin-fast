package com.filecoin.modules.sys.controller;

import com.filecoin.modules.filecoin.service.DInvitationCodeInfoService;
import com.filecoin.modules.filecoin.service.SysUserExtendService;
import com.filecoin.modules.sys.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密码修改及找回相关
 * 
 * @author r25437,g20416
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



}
