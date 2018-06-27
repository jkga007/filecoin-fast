package com.filecoin.modules.job.task;



import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 短信发送定时任务
 * 
 * sendMessageTask为spring bean的名称
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2016年11月30日 下午1:34:24
 */
@Component("sendMessageTask")
public class SendMessageTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SysUserService sysUserService;

	//定时任务只能接受一个参数；如果有多个参数，使用json数据即可
	public void test(String params){
		logger.info("我是带参数的test方法，正在被执行，参数为：" + params);
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SysUserEntity user = sysUserService.queryObject(1L);
		System.out.println(ToStringBuilder.reflectionToString(user));
		
	}
	
	
	public void test2(){
		logger.info("我是不带参数的test2方法，正在被执行");
	}
}
