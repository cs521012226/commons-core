package org.ltsh.core.extend.task;

import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJob implements Job{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private String cronSchedule;
	private String jobName;
	
	public AbstractJob(String cronSchedule){
		this(cronSchedule, null);
	}
	
	public AbstractJob(String cronSchedule, String jobName){
		this.jobName = jobName == null ? getClass().getName() : jobName;
		this.cronSchedule = cronSchedule;
	}

	public String getCronSchedule() {
		return cronSchedule;
	}
	public String getJobName() {
		return jobName;
	}
}
