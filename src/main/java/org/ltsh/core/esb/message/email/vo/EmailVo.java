package org.ltsh.core.esb.message.email.vo;


/**
 * 邮件实体类
 * @author 邓端宁
 * @date 2018年4月8日
 */
public class EmailVo {
	private int timeout = 60 * 1000; 
	private String host;
	private String senderEmail;
	private String senderPwd;
	private String receiverEmail;
	private String [] receiver;
	private String subject;
	private String msg;
	
	
	public EmailVo() {
	}

	public EmailVo(String host, String senderEmail, String senderPwd) {
		this.host = host;
		this.senderEmail = senderEmail;
		this.senderPwd = senderPwd;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderPwd() {
		return senderPwd;
	}

	public void setSenderPwd(String senderPwd) {
		this.senderPwd = senderPwd;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String[] getReceiver() {
		return receiver;
	}

	public void setReceiver(String[] receiver) {
		this.receiver = receiver;
	}

}
