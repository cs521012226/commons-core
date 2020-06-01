package org.ltsh.core.esb.message.email;

import org.apache.commons.mail.EmailException;

import org.ltsh.core.esb.message.email.vo.EmailVo;

/**
 * 邮箱服务统一接口
 * @author 邓端宁
 * @date 2018年4月4日
 */
public interface EmailService {
	/**
	 * 发送邮件
	 * @param emailVo 
	 * @throws EmailException
	 */
	void send(EmailVo emailVo) throws EmailException;
	/**
	 * 批量发送邮件
	 * @param emailVo
	 * @throws EmailException
	 */
	void batchSend(EmailVo emailVo) throws EmailException;
}
