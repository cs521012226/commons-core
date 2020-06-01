package org.ltsh.core.core.pool.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消费者模式线程任务池
 * @author Ych
 * 2017年7月27日
 */
public class ThreadTaskPool {
	public static Logger logger = LoggerFactory.getLogger(ThreadTaskPool.class);
	
	private ExecutorService threadPool;
	private BlockingQueue<ThreadTask> queue;
	
	public ThreadTaskPool(int fixedPoolSize){
		this(Executors.newFixedThreadPool(fixedPoolSize));
	}
	
	public ThreadTaskPool(ExecutorService executorService){
		this.threadPool = executorService;
		this.queue = new LinkedBlockingQueue<ThreadTask>();
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						threadPool.execute(new Runner(queue.take()));
					}
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				} finally {
					threadPool.shutdown();
				}
			}
		});
	}
	
	private class Runner implements Runnable{
		private ThreadTask task;
		
		public Runner(ThreadTask task){
			this.task = task;
		}
		@Override
		public void run() {
			task.execute();
		}
	}
	
	/**
	 * 生产者用来添加线程任务
	 * @author Ych
	 * @param runner
	 */
	public void add(ThreadTask task){
		try {
			queue.put(task);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
