/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.service;

import java.util.List;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoDao;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfoMeeting;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoMeetingDao;

/**
 * 项目立项审批Service
 * @author lhy
 * @version 2017-01-04
 */
@Service
@Transactional(readOnly = true)
public class ProjectInfoMeetingService extends CrudService<ProjectInfoMeetingDao, ProjectInfoMeeting> {

	@Autowired
	private ProjectInfoMeetingDao projectInfoMeetingDao;

	@Autowired
	private ProjectInfoDao projectInfoDao;


	public ProjectInfoMeeting get(String id) {
		return super.get(id);
	}
	
	public List<ProjectInfoMeeting> findList(ProjectInfoMeeting projectInfoMeeting) {
		return super.findList(projectInfoMeeting);
	}
	
	public Page<ProjectInfoMeeting> findPage(Page<ProjectInfoMeeting> page, ProjectInfoMeeting projectInfoMeeting) {
		return super.findPage(page, projectInfoMeeting);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectInfoMeeting projectInfoMeeting) {
		super.save(projectInfoMeeting);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProjectInfoMeeting projectInfoMeeting) {
		super.delete(projectInfoMeeting);
	}

	@Transactional(readOnly = false)
	public void addProjectInfoMeeting(ProjectInfo projectInfo, ProjectInfoMeeting projectInfoMeeting) {

		projectInfo.setProjectStatus(projectInfoMeeting.getStatusCurrent());
		projectInfo.setUpdateBy(UserUtils.getUser());
		projectInfo.setUpdateDate(DateUtils.getCurrentDateTime());
		projectInfoDao.update(projectInfo);

		projectInfoMeeting.setProjectInfoId(projectInfo.getId());
		projectInfoMeeting.preInsert();
		projectInfoMeetingDao.insert(projectInfoMeeting);

	}
}