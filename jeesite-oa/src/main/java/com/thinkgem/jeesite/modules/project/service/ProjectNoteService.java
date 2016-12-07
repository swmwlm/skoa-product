/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.oa.service.OaNotifyService;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoDao;
import com.thinkgem.jeesite.modules.project.dao.ProjectNoteDao;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.entity.ProjectNote;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 项目动态表管理Service
 * @author lhy
 * @version 2016-11-07
 */
@Service
@Transactional(readOnly = true)
public class ProjectNoteService extends CrudService<ProjectNoteDao, ProjectNote> {

	@Autowired
	private OaNotifyService oaNotifyService;

	@Autowired
	private ProjectInfoDao projectInfoDao;

	public ProjectNote get(String id) {
		return super.get(id);
	}
	
	public List<ProjectNote> findList(ProjectNote projectNote) {
		return super.findList(projectNote);
	}
	
	public Page<ProjectNote> findPage(Page<ProjectNote> page, ProjectNote projectNote) {
		return super.findPage(page, projectNote);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectNote projectNote) {
		//1.先保存项目评论
		super.save(projectNote);
		//2.再做通知通告,发消息
//		if (StringUtils.isNotBlank(projectNote.getAtUserids())) {
			ProjectInfo projectInfo=projectInfoDao.get(projectNote.getProjectId());

			//根据项目获取需要通知的人员
			String oaNotifyRecordIds=UserUtils.getNotifyUserIdsString(projectInfo, projectNote.getAtUserids(),UserUtils.getUser().getId());
			System.out.println("【项目动态添加：oaNotifyRecordIds:"+oaNotifyRecordIds+"】");

			//邮件通知等(预留)
			//添加到我的通告
			OaNotify oaNotify = new OaNotify();
			oaNotify.setType("4");
			oaNotify.setTitle(projectInfo.getProjectName());
			oaNotify.setContent(projectNote.getContent());
			oaNotify.setStatus("1");
			oaNotify.setOaNotifyRecordIds(oaNotifyRecordIds);
			oaNotify.setRemarks(projectInfo.getId());
			oaNotifyService.save(oaNotify);
//		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ProjectNote projectNote) {
		super.delete(projectNote);
	}
	
}