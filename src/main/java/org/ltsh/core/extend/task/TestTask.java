package org.ltsh.core.extend.task;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * @author Ych
 * 2018年11月9日
 */
public class TestTask {
	protected static Logger logger = LoggerFactory.getLogger(TestTask.class);

	public static class Job1 extends AbstractJob{
		
		public Job1(){
			super("0/10 * * * * ?");
		}

		@Override
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
			System.out.println(new Date() + "#### execute Job1");
		}
	}
	public static class Job2 extends AbstractJob{
		public Job2(){
			super("3/5 * * * * ?");
		}
		@Override
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
			System.out.println(new Date() + "**************** execute Job2");
		}
	}
	
	public static void main(String[] args) {
		
		BaseCronSchedule bcs = new BaseCronSchedule();
		
		bcs.addJob(new Job1());
		bcs.addJob(new Job2());
		bcs.start();
	}

}
