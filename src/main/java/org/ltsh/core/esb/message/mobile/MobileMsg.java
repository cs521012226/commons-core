package org.ltsh.core.esb.message.mobile;

import java.io.IOException;

/**
 * 短信客户端
 * @author Ych
 * 2017年8月16日
 */
public interface MobileMsg {
	/**
	 * 设置编码
	 * @author Ych
	 * @param charset
	 */
	void setCharset(String charset); //设置Charset 
	
	/**
	 * 设置电话
	 * @author Ych
	 * @param mobile
	 */
	void setMobile(String mobile);
	/**
	 * 设置消息内容
	 * @author Ych
	 * @param message
	 */
	void setContent(String content); //内容 
	/**
	 * 发送消息
	 * @author Ych
	 */
	void send() throws IOException;
}
