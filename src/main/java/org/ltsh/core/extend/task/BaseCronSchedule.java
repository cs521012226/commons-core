package org.ltsh.core.extend.task;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础通用CronSchedule调度器
 * @author Ych
 * 2019年2月2日
 */
public class BaseCronSchedule {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private List<AbstractJob> jobList = new ArrayList<AbstractJob>();
	private boolean started;
	private String groupName = "GroupName";
	private Scheduler scheduler;
	
	public BaseCronSchedule(){
		try {
			// 创建scheduler
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	public void addJob(AbstractJob job){
		jobList.add(job);
	}
	
	
	/**
	 * 开启任务调度
	 * @author Ych
	 */
	public synchronized void start(){
		if(started){
			return ;
		}
		try {
			for (AbstractJob job : jobList) {

				String name = job.getJobName();

				// 创建jobDetail实例，绑定Job实现类
				// 指明job的名称，所在组的名称，以及绑定job类
				JobDetail jobDetail = JobBuilder.newJob(job.getClass())
						.withIdentity(name + "JobDetail", groupName).build();

				// 定义调度触发规则
				// corn表达式 每五秒执行一次
				Trigger trigger = TriggerBuilder
						.newTrigger()
						.withIdentity(name + "CronTrigger", groupName)
						.withSchedule(
								CronScheduleBuilder.cronSchedule(job
										.getCronSchedule())).startNow().build();
				// 把作业和触发器注册到任务调度中
				scheduler.scheduleJob(jobDetail, trigger);
			}

			// 启动调度
			scheduler.start();
			
			started = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 关闭任务调调
	 * @author Ych
	 */
	public synchronized void stop(){
		if(started){
			try {
				scheduler.shutdown();
				started = false;
			} catch (SchedulerException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
