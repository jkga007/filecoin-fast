package com.filecoin.common.exception;

import com.filecoin.common.utils.JsonResult;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2016年10月27日 下午10:16:19
 */
@RestControllerAdvice
public class FileCoinExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(FileCoinException.class)
	public JsonResult handleRRException(FileCoinException e){
		JsonResult jsonResult = new JsonResult();
		jsonResult.put("code", e.getCode());
		jsonResult.put("msg", e.getMessage());

		return jsonResult;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public JsonResult handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return JsonResult.error("数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public JsonResult handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return JsonResult.error("没有权限，请联系管理员授权");
	}

	@ExceptionHandler(Exception.class)
	public JsonResult handleException(Exception e){
		logger.error(e.getMessage(), e);
		return JsonResult.error();
	}
}
