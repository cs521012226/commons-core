package org.ltsh.core.esb.client.base;

import org.ltsh.core.esb.message.EsbMsgGetter;

/**
 * Esb服务的响应 数据接口
 * @author Ych
 * 2017年11月20日
 */
public interface EsbSenderResponse extends EsbMsgGetter {
	/**
	 * 是否成功
	 * @author Ych
	 * @param success
	 */
	void setSuccess(boolean success);
	
	/**
	 * 是否成功
	 * @author Ych
	 * @return
	 */
	boolean isSuccess();
	
	/**
	 * 设置返回信息
	 * @author Ych
	 * @param msg
	 */
	void setMsg(String msg);
	
	/**
	 * 获取返回数据
	 * @author Ych
	 * @return
	 */
	String getMsg();
	
	/**
	 * 判断Body数据是否为空
	 * @author Ych
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * 设置错误代码
	 * @author Ych
	 */
	void setCode(String code);
	
	/**
	 * 获取错误代码
	 * @author Ych
	 * @return
	 */
	String getCode();
	
}
