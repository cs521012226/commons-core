package org.ltsh.core.test.email;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.ltsh.core.esb.message.email.EmailService;
import org.ltsh.core.esb.message.email.imp.EmailServiceImpl;
import org.ltsh.core.esb.message.email.vo.EmailVo;

/**
 * @author 邓端宁
 * @date 2018年4月8日
 */
public class TestEmail {
	
	public static void main(String[] args) {
//		PropertyConfigurator.configure(Server.class.getResource("/com/nybank/test/log4j.properties"));
		EmailService emailService = new EmailServiceImpl();
		EmailVo emailVo = new EmailVo("11.8.129.161", "ps@gdnybank.com", "password!23");
//		emailVo.setReceiverEmail("dengdn@gdnybank.com");
		emailVo.setSubject("人力系统测试邮件");
		emailVo.setMsg("人力系统测试邮件");
		try {
			List<String> receiverEmailList = new ArrayList<>();
			receiverEmailList.add("dengdn@gdnybank.com");
			receiverEmailList.add("ljin@gdnybank.com");
			receiverEmailList.add("lifeng@gdnybank.com");
			emailVo.setReceiver((String[])receiverEmailList.toArray());
			emailService.batchSend(emailVo);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
}
