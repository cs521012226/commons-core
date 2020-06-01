package org.ltsh.core.esb.message.wechat.vo;

import com.alibaba.fastjson.JSON;

/**
 * 报文信息状态
 * @author Ych
 * 2018年4月17日
 */
public class MsgInfoStatus {
	private String id;
	private String name;
	private String mail;
	private String mobile;
	private String title;
	private String content;
	
	private boolean success;
	private String code;
	private String message;
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	/**
	 * 设置状态
	 * @author Ych
	 * @param success
	 * @param code
	 * @param message
	 */
	public void setStatus(boolean success, String code, String message){
		this.success = success;
		this.code = code;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
