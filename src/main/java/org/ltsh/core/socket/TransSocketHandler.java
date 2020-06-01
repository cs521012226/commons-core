package org.ltsh.core.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.ltsh.core.core.pool.thread.ThreadTask;
import org.ltsh.core.core.util.FileUtil;
import org.ltsh.core.esb.exp.TransException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据传输
 * @author Ych
 * 2017年7月24日
 */
public class TransSocketHandler implements ThreadTask{
	public static Logger logger = LoggerFactory.getLogger(TransSocketHandler.class);
	
	private InputStream input;
	private OutputStream output;
	
	private TransFlow transFlow;
	private boolean keepConnect = false;	//是否保持长连接
	
	public TransSocketHandler(Socket client, TransFlow transFlow){
		try {
			this.input = client.getInputStream();
			this.output = client.getOutputStream();
			this.transFlow = transFlow;
		
		} catch (IOException e) {
			throw TransException.error("初始化TransSocketHandler客户端失败", e);
		}
	}
	

	@Override
	public void execute() {
		try {
			transFlow.process(input, output);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if(!keepConnect){
				close();
			}
		}
	}
	/**
	 * 关闭流
	 * @author Ych
	 * 2017年7月24日
	 */
	public void close(){
		FileUtil.close(input, output);
	}

	public TransFlow getTransFlow() {
		return transFlow;
	}

	public void setTransFlow(TransFlow transFlow) {
		this.transFlow = transFlow;
	}
	public void setKeepConnect(boolean keepConnect) {
		this.keepConnect = keepConnect;
	}

}
