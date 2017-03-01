/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 项目立项审批Entity
 * @author lhy
 * @version 2017-01-04
 */
public class ProjectInfoMeeting extends DataEntity<ProjectInfoMeeting> {
	
	private static final long serialVersionUID = 1L;
	private String projectInfoId;		// 项目id
	private String projectInfoName;		// 项目id
	private String statusOrigin;		// 项目状态-更新前
	private String statusCurrent;		// 项目状态-更新后
	private String filepath;		// 附件路径
	
	public ProjectInfoMeeting() {
		super();
	}

	public ProjectInfoMeeting(String id){
		super(id);
	}

	@Length(min=0, max=64, message="项目id长度必须介于 0 和 64 之间")
	public String getProjectInfoId() {
		return projectInfoId;
	}

	public void setProjectInfoId(String projectInfoId) {
		this.projectInfoId = projectInfoId;
	}
	
	@Length(min=0, max=2, message="项目状态-更新前长度必须介于 0 和 2 之间")
	public String getStatusOrigin() {
		return statusOrigin;
	}

	public void setStatusOrigin(String statusOrigin) {
		this.statusOrigin = statusOrigin;
	}
	
	@Length(min=0, max=2, message="项目状态-更新后长度必须介于 0 和 2 之间")
	public String getStatusCurrent() {
		return statusCurrent;
	}

	public void setStatusCurrent(String statusCurrent) {
		this.statusCurrent = statusCurrent;
	}
	
	@Length(min=0, max=2000, message="附件路径长度必须介于 0 和 2000 之间")
	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getProjectInfoName() {
		return projectInfoName;
	}

	public void setProjectInfoName(String projectInfoName) {
		this.projectInfoName = projectInfoName;
	}
}