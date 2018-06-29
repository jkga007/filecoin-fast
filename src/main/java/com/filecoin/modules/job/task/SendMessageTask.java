package com.filecoin.modules.job.task;



import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.filecoin.common.utils.Constant;
import com.filecoin.common.utils.SmsSendUtil;
import com.filecoin.modules.filecoin.entity.WSendMessageEntity;
import com.filecoin.modules.filecoin.service.WSendMessageService;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		Map<String,Object> paramsMap = new HashMap<>();
		paramsMap.put("offset",0);
		paramsMap.put("limit",50);
		paramsMap.put("status",0);
		List<WSendMessageEntity> wSendMessageEntityList = wSendMessageService.queryList(paramsMap);
		for(WSendMessageEntity wSendMessageEntity:wSendMessageEntityList){
			Map<String,Object> smsMap = new HashMap<>();
			smsMap.put("template_code", Constant.IDENTIFYING_CODE_SMS_TEMPLATE_CODE);
			smsMap.put("mobile",wSendMessageEntity.getMobile());
			smsMap.put("code",wSendMessageEntity.getIdentifyingCode());
			smsMap.put("template_param","{\"code\":\""+wSendMessageEntity.getIdentifyingCode()+"\"}");

			WSendMessageEntity wSendMessageEntityUpdate = new WSendMessageEntity();
			wSendMessageEntityUpdate.setId(wSendMessageEntity.getId());
			try {
				SendSmsResponse sendSmsResponse = SmsSendUtil.sendSms(smsMap);
				wSendMessageEntityUpdate.setStatus(Constant.SendStatus.SUCCESS.getValue());
				wSendMessageEntityUpdate.setReturnCode(sendSmsResponse.getCode());
				wSendMessageEntityUpdate.setReturnMessage(sendSmsResponse.getMessage());
				wSendMessageEntityUpdate.setRequestId(sendSmsResponse.getRequestId());
				wSendMessageEntityUpdate.setBizId(sendSmsResponse.getBizId());
			}catch(Exception e){
				logger.error(e.getMessage());
				wSendMessageEntityUpdate.setStatus(Constant.SendStatus.ERROR.getValue());
				wSendMessageEntityUpdate.setReturnCode("ERROR");
				wSendMessageEntityUpdate.setReturnMessage("Send Exception");
			}finally{
				wSendMessageService.update(wSendMessageEntityUpdate);
			}
		}
	}
}
