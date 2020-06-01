package org.ltsh.core.socket;

import java.net.ServerSocket;
import java.net.Socket;

import org.ltsh.core.core.pool.thread.ThreadTask;
import org.ltsh.core.core.pool.thread.ThreadTaskPool;
import org.ltsh.core.esb.exp.TransException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * socket监听服务
 * @author YeChao
 * 2017年7月24日
 */
public abstract class AbstractTransSocketServer implements ThreadTask{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private int port;
	private int timeout;
	private int poolSize;
	
	private ServerSocket server;
	
	private ThreadTaskPool threadPool;
	
	public AbstractTransSocketServer(int port, int poolSize){
		this.port = port;
		this.poolSize = poolSize;
	}
	
	/**
	 * 开启Socket服务监听
	 * @author Ych
	 */
	public void start(){
		this.threadPool = new ThreadTaskPool(poolSize);
		threadPool.add(this);
	}
	
	@Override
	public void execute() {
		try {
			server = new ServerSocket(port);
			logger.info("Socket监听服务启动成功. 监听端口：" + port + ", 使用线程池大小：" + poolSize + ", 超时时间设定为：" + timeout + "（秒）");
			
			while(true){
				Socket socket = server.accept();
				if(timeout != 0){
					socket.setSoTimeout(timeout * 1000);
				}
				//添加任务
				threadPool.add(buildHandler(socket));
			}
			
		} catch (Exception e) {
			throw TransException.error("Socket监听服务启动失败. 监听端口：" + port, e);
		}
	}
	
	/**
	 * 构建处理器
	 * @author Ych
	 * @param socket
	 * @return
	 */
	protected abstract ThreadTask buildHandler(Socket socket);

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
