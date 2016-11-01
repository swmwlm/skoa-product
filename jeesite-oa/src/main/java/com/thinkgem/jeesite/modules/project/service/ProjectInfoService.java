/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.service;

import com.google.common.base.Joiner;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoDao;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoProgressDao;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfoProgress;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 项目管理Service
 * @author evan
 * @version 2016-10-25
 */
@Service
@Transactional(readOnly = true)
public class ProjectInfoService extends CrudService<ProjectInfoDao, ProjectInfo> {

	@Autowired
	private ProjectInfoProgressDao projectInfoProgressDao;
	
	public ProjectInfo get(String id) {
		ProjectInfo projectInfo = super.get(id);
		projectInfo.setProjectInfoProgressList(projectInfoProgressDao.findList(new ProjectInfoProgress(projectInfo)));
		return projectInfo;
	}
	
	public List<ProjectInfo> findList(ProjectInfo projectInfo) {
		return super.findList(projectInfo);
	}

	/**
	 * 带数据访问权限的查询项目列表data scope filter
	 * @param page
	 * @param projectInfo
	 * @return
	 */
	public Page<ProjectInfo> findPageDSF(Page<ProjectInfo> page, ProjectInfo projectInfo) {

		StringBuffer sb=new StringBuffer();
		User currentUser=UserUtils.getUser();

		sb.append(" and (");
		//1.所有人都可以看到项目进度为0或者1的项目
		sb.append(" a.project_progress<2 ");
		//2.当前用户可以看到 自己创建的项目,并且该项目进度为空或者为0或者为1
		sb.append(" or (a.create_by = '"+currentUser.getId()+"' and (a.project_progress is null or a.project_progress<2 )) ");
		//3.当前用户可以看到 自己负责的项目
		sb.append(" or a.primary_person='"+currentUser.getId()+"' ");
		//4.当前用户可以看到 自己参与的(所在项目小组)项目,并且项目进度小于5
		sb.append(" or (find_in_set('"+currentUser.getId()+"',a.team_members) and a.project_progress<5)");
		//5.当前用户可以看到 按 自己所在角色与项目进度绑定的集合 ,进行筛选
		Set<String> progressSet= UserUtils.getProjectProgressSet();//获取该用户拥有的项目进度;
		if(CollectionUtils.isNotEmpty(progressSet)){
			String progressIn= "'"+Joiner.on("','").skipNulls().join(progressSet)+"'";
			sb.append(" or a.project_progress in ("+progressIn+")");
		}

		sb.append(")");

		projectInfo.getSqlMap().put("dsf",sb.toString());
		return super.findPage(page, projectInfo);
	}
	public Page<ProjectInfo> findPage(Page<ProjectInfo> page, ProjectInfo projectInfo) {
		return super.findPage(page, projectInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectInfo projectInfo) {
		super.save(projectInfo);
		for (ProjectInfoProgress projectInfoProgress : projectInfo.getProjectInfoProgressList()){
			if (projectInfoProgress.getId() == null){
				continue;
			}
			if (ProjectInfoProgress.DEL_FLAG_NORMAL.equals(projectInfoProgress.getDelFlag())){
				if (StringUtils.isBlank(projectInfoProgress.getId())){
					projectInfoProgress.setProjectInfo(projectInfo);
					projectInfoProgress.preInsert();
					projectInfoProgressDao.insert(projectInfoProgress);
				}else{
					projectInfoProgress.preUpdate();
					projectInfoProgressDao.update(projectInfoProgress);
				}
			}else{
				projectInfoProgressDao.delete(projectInfoProgress);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(ProjectInfo projectInfo) {
		super.delete(projectInfo);
		//projectInfoProgressDao.delete(new ProjectInfoProgress(projectInfo));
	}
	
}