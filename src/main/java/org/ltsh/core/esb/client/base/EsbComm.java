package org.ltsh.core.esb.client.base;

import java.io.IOException;

/**
 * ESB通讯协议接口
 * @author Ych
 * 2018年4月16日
 */
public interface EsbComm {

	
	/**
	 * 发送消息
	 * @author Ych
	 * @param message
	 * @return
	 * @throws IOException
	 */
	String send(String message) throws IOException;
}
