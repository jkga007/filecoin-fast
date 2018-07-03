package com.filecoin.modules.job.utils;


import com.filecoin.common.exception.FileCoinException;
import com.filecoin.common.utils.SpringContextUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 执行定时任务
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2016年11月30日 下午12:49:33
 */
public class ScheduleRunnable implements Runnable {
	private Object target;
	private Method method;
	private String params;
	
	public ScheduleRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
		target = SpringContextUtils.getBean(beanName);
		this.params = params;
		
		if(StringUtils.isNotBlank(params)){
			method = target.getClass().getDeclaredMethod(methodName, String.class);
		}else{
			method = target.getClass().getDeclaredMethod(methodName);
		}
	}

	@Override
	public void run() {
		try {
			System.out.println("查看当前线程名称-------------" + Thread.currentThread().getName() + "--ID==" + Thread.currentThread().getId());
			ReflectionUtils.makeAccessible(method);
			if(StringUtils.isNotBlank(params)){
				method.invoke(target, params);
			}else{
				method.invoke(target);
			}
		}catch (Exception e) {
			throw new FileCoinException("执行定时任务失败", e);
		}
	}

}
