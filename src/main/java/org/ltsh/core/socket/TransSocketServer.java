package org.ltsh.core.socket;

import java.net.Socket;

import org.ltsh.core.core.pool.thread.ThreadTask;

/**
 * socket监听服务
 * @author YeChao
 * 2017年7月24日
 */
public class TransSocketServer extends AbstractTransSocketServer {
	private boolean keepConnect = false;	//是否保持长连接
	
	private TransFlow transFlow;
	public TransSocketServer(TransFlow transFlow, int port, int poolSize){
		super(port, poolSize);
		this.transFlow = transFlow;
	}
	
	/**
	 * 构建
	 * @author Ych
	 * @param socket
	 * @return
	 */
	@Override
	protected ThreadTask buildHandler(Socket socket){
		TransSocketHandler tsc = new TransSocketHandler(socket, transFlow);
		tsc.setKeepConnect(keepConnect);
		return tsc;
	}
	

	public void setKeepConnect(boolean keepConnect) {
		this.keepConnect = keepConnect;
	}
}
