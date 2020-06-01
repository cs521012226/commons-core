package org.ltsh.core.socket;

import java.io.InputStream;
import java.io.OutputStream;

import org.ltsh.core.esb.exp.TransException;

/**
 * 传输数据主要监听接口
 * @author Ych
 * 2017年7月24日
 */
public interface TransFlow {
	
	/**
	 * 连接成功后触发
	 * @author Ych
	 * @param input
	 * @param output
	 * @throws TransException
	 */
	void process(InputStream input, OutputStream output) throws TransException;
}
