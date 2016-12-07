/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.batchlog.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 任务日志Entity
 * @author evan
 * @version 2016-12-07
 */
public class SysBatchlog extends DataEntity<SysBatchlog> {
	
	private static final long serialVersionUID = 1L;
	private String jobname;		// 任务名称
	private Date lastexecutetime;		// 上次执行时间
	
	public SysBatchlog() {
		super();
	}

	public SysBatchlog(String id){
		super(id);
	}

	@Length(min=0, max=64, message="任务名称长度必须介于 0 和 64 之间")
	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getLastexecutetime() {
		return lastexecutetime;
	}

	public void setLastexecutetime(Date lastexecutetime) {
		this.lastexecutetime = lastexecutetime;
	}
	
}