/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfoMeeting;

/**
 * 项目立项审批DAO接口
 * @author lhy
 * @version 2017-01-04
 */
@MyBatisDao
public interface ProjectInfoMeetingDao extends CrudDao<ProjectInfoMeeting> {
	
}