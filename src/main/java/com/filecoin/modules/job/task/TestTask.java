package com.filecoin.modules.job.task;



import com.filecoin.modules.filecoin.service.WSendMessageService;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 测试定时任务(演示Demo，可删除)
 * 
 * testTask为spring bean的名称
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2016年11月30日 下午1:34:24
 */
@Component("testTask")
public class TestTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private WSendMessageService wSendMessageService;

	//定时任务只能接受一个参数；如果有多个参数，使用json数据即可
	public void batchSendMessageByParam(String params){
		logger.info("我是带参数的batchSendMessage方法，正在被执行，参数为：" + params);
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//定时任务，无需参数的
	public void batchSendMessage(){
		logger.info("我是不带参数的batchSendMessage方法，正在被执行");
		//获取待发送短信列表 15秒执行一次，一次发送50条

	}
}
