package org.ltsh.core.esb.builder;

import org.ltsh.core.esb.vo.MsgNode;

/**
 * 报文格式转换器
 * @author Ych
 * 2018年4月16日
 */
public interface MsgConvert {

	/**
	 * 对象转为报文
	 * @author Ych
	 * @param node
	 * @return
	 */
	String toMessage(MsgNode node);
	
	/**
	 * 报文转为对象
	 * @author Ych
	 * @param message
	 * @return
	 */
	MsgNode toObject(String message);
}
