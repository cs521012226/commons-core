package org.ltsh.core.esb.server;

import org.ltsh.core.esb.message.EsbMsgGetter;
import org.ltsh.core.esb.message.EsbMsgSetter;



/**
 * 响应Esb发过来的请求数据接口
 * @author YeChao
 * 2017年7月25日
 */
public interface EsbReceiverResponse extends EsbMsgSetter, EsbMsgGetter{
	
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
