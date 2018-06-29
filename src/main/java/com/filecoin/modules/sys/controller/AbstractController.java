package com.filecoin.modules.sys.controller;

import com.filecoin.common.utils.HttpContextUtils;
import com.filecoin.common.utils.IPUtils;
import com.filecoin.modules.sys.entity.SysLogEntity;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Controller公共组件
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}
}
