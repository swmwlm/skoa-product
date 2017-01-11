/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.service;

import java.util.List;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.oa.service.OaNotifyService;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoDao;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
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

	@Autowired
	private OaNotifyService oaNotifyService;

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
	public void addProjectInfoMeeting(ProjectInfo projectInfo, String remarks, String statusOrigin, String statusCurrent) {

		ProjectInfoMeeting projectInfoMeeting = new ProjectInfoMeeting();
		projectInfoMeeting.setId("");
		projectInfoMeeting.setRemarks(remarks);
		projectInfoMeeting.setStatusOrigin(statusOrigin);
		projectInfoMeeting.setStatusCurrent(statusCurrent);
		projectInfoMeeting.setProjectInfoId(projectInfo.getId());
		projectInfoMeeting.preInsert();
		projectInfoMeetingDao.insert(projectInfoMeeting);

		projectInfo.setProjectStatus(statusCurrent);
		projectInfo.setUpdateBy(UserUtils.getUser());
		projectInfo.setUpdateDate(DateUtils.getCurrentDateTime());
		projectInfoDao.update(projectInfo);

		//根据项目获取需要通知的人员
		String oaNotifyRecordIds = UserUtils.getMeetingNotifyUserIdsString(projectInfoMeeting, UserUtils.getUser().getId());
		//当接收人为空时,不需要发布一个通知了;
		if (StringUtils.isBlank(StringUtils.strip(oaNotifyRecordIds, ",")))
			return;

		StringBuilder content = new StringBuilder();
		content.append("项目");
//		1，提交初审
		if (statusOrigin.equals("0") && statusCurrent.equals("4")) {
			content.append("已提交立项会初审，请审批。");
		}
//		2，提交复审
		if (statusOrigin.equals("4") && statusCurrent.equals("5")) {
			content.append("已提交立项会复审，请审批。");
		}
//		3，初审驳回
		if (statusOrigin.equals("4") && statusCurrent.equals("0")) {
			content.append("立项会初审未通过。");
		}
//		4，复审驳回
		if (statusOrigin.equals("5") && statusCurrent.equals("0")) {
			content.append("立项会复审未通过。");
		}
		if (StringUtils.isNotBlank(remarks)) {
			content.append(" 备注:" + remarks);
		}
		//添加到我的通告
		OaNotify oaNotify = new OaNotify();
		oaNotify.setType("6");
		oaNotify.setTitle(projectInfo.getProjectName());
		oaNotify.setContent(content.toString());
		oaNotify.setStatus("1");
		oaNotify.setOaNotifyRecordIds(oaNotifyRecordIds);
		oaNotify.setRemarks(projectInfo.getId());
		oaNotifyService.save(oaNotify);
		System.out.println("【项目立项会审批消息通知：(通知人员ids:" + oaNotifyRecordIds + "),（通知内容:" + content.toString() + ")】");
	}

}