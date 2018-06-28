package com.filecoin.modules.job.task;



import com.filecoin.common.exception.FileCoinException;
import com.filecoin.common.utils.Constant;
import com.filecoin.common.utils.JsonResult;
import com.filecoin.common.utils.SnowflakeIdWorker;
import com.filecoin.modules.filecoin.entity.WSendEmailEntity;
import com.filecoin.modules.filecoin.service.WSendEmailService;
import com.filecoin.modules.sys.entity.SysUserEntity;
import com.filecoin.modules.sys.service.SysUserService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * 短信发送定时任务
 * 
 * sendMessageTask为spring bean的名称
 * 
 * @author r25437,g20416
 * @email support@filecoinon.com
 * @date 2016年11月30日 下午1:34:24
 */
@Component("sendEmailTask")
public class SendEmailTask {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private WSendEmailService wSendEmailService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TemplateEngine templateEngine;
	@Value("${spring.mail.username}")
	private String sender;

	//定时任务只能接受一个参数；如果有多个参数，使用json数据即可
	public void sendEmailJob(String params){
		logger.info("我是带参数的sendEmailJob方法，正在被执行，参数为：" + params);
		
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		SnowflakeIdWorker idWorker0 = new SnowflakeIdWorker(0, 0);

		WSendEmailEntity wSendEmailEntity = wSendEmailService.queryOneNearBy();
		if(wSendEmailEntity != null){
			JsonResult jsonResult = sendTemplateMail(wSendEmailEntity.getEmail(),wSendEmailEntity.getUserId(),idWorker0.timeGen1());

			wSendEmailEntity.setStatus(Integer.parseInt(String.valueOf(jsonResult.get("flag"))));
			wSendEmailEntity.setSendTime(new Date());
			wSendEmailEntity.setReturnCode(String.valueOf(jsonResult.get("flag")));
			wSendEmailEntity.setReturnMessage(String.valueOf(jsonResult.get("msg")));
			//修改状态
			wSendEmailService.update(wSendEmailEntity);
		}
	}

	public JsonResult sendTemplateMail(String recipient, Long userId, Long timestamp) {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(recipient);
			helper.setSubject("IPFS星际云矿池");
			Context context = new Context();
			context.setVariable("id", userId);
			context.setVariable("timestamp", timestamp);
			context.setVariable("url", "120.79.242.64");
			context.setVariable("other", "utm_campaign=filecoin-email-verification&utm_content=html&utm_medium=email&utm_source=verification-email");
			String emailContent = templateEngine.process("emailTemplate", context);
			helper.setText(emailContent, true);
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return JsonResult.error("CREATE_MSG_ERROR").put("flag",Constant.SendStatus.ERROR.getValue());
		}
		try{
			javaMailSender.send(message);
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return JsonResult.error("SEND_ERROR").put("flag",Constant.SendStatus.ERROR.getValue());
		}
		return JsonResult.ok("SEND_SUCCESS").put("flag",Constant.SendStatus.SUCCESS.getValue());
	}
	
	
	public void test2(){
		logger.info("我是不带参数的test2方法，正在被执行");
	}
}