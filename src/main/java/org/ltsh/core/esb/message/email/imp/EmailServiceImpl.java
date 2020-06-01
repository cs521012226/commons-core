package org.ltsh.core.esb.message.email.imp;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.ltsh.core.core.constants.Config;
import org.ltsh.core.esb.message.email.EmailService;
import org.ltsh.core.esb.message.email.vo.EmailVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件服务统一	
 * @author 邓端宁
 * @date 2018年4月4日
 */
public class EmailServiceImpl implements EmailService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void send(EmailVo emailVo) throws EmailException {
		try {
			Email email = new SimpleEmail();
			email.setSocketConnectionTimeout(emailVo.getTimeout());
//			email.setTLS(true);
			email.setHostName(emailVo.getHost());
			email.setAuthentication(emailVo.getSenderEmail(), emailVo.getSenderPwd());
			//发送邮箱
			email.setFrom(emailVo.getSenderEmail());
			//接收邮箱
			email.addTo(emailVo.getReceiverEmail());
			 //设置字符集
			email.setCharset(Config.CHARSET);
			email.setSubject(emailVo.getSubject());
			email.setMsg(emailVo.getMsg());
			email.send();
			logger.info("发送邮件给：" + emailVo.getReceiverEmail() + "成功.");
		} catch (Exception e) {
			logger.error("发送邮件给：" + emailVo.getReceiverEmail() + "失败.",e);
		}
	}

	@Override
	public void batchSend(EmailVo emailVo) throws EmailException {
		String [] receiver = emailVo.getReceiver();
		if(receiver == null || receiver.length == 0)  {
			logger.error("没有接收人，无需发送邮件.");
			return;
		}
		logger.info("开始批量发送邮件.");
		for (String r : receiver) {
			emailVo.setReceiverEmail(r);
			send(emailVo);
		}
		logger.info("批量发送邮件结束.");
	}
	
}
