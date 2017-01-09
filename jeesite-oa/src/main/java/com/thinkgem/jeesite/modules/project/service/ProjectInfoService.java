/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.service;

import com.google.common.base.Joiner;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoDao;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoProgressDao;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfoProgress;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
		return this.findPageDSFforAPP(page,projectInfo,UserUtils.getUser().getId());
	}


	/**
	 * 带数据访问权限的查询项目列表data scope filter（APP调用API使用）
	 *
	 * @param page
	 * @param projectInfo
	 * @return
	 */
	public Page<ProjectInfo> findPageDSFforAPP(Page<ProjectInfo> page, ProjectInfo projectInfo, String userId) {

		StringBuffer sb=new StringBuffer();

		sb.append(" and (");
		//1.当前用户可以看到项目进度为0或者1的项目,且项目状态不为 个人编辑 状态的
		sb.append(" (a.project_progress<2 and a.project_status!=0)");
		//1.2 当前用户可以看到 项目进度为null,且项目状态为4,且项目所属部门的主副负责人为当前用户的项目;
		sb.append(" or (a.project_progress is null and a.project_status=4 and (o2.PRIMARY_PERSON ='"+userId+"' or o2.DEPUTY_PERSON='"+userId+"') ) ");
		//1.3 当前用户可以看到 项目进度为null,且项目状态为5,且当前用户的角色为合伙人的项目
		sb.append(" or (a.project_progress is null and a.project_status=5 and "+UserUtils.isPartnerRole(userId)+" ) ");
		//1.4 项目状态为 1发布状态 下,当项目进度为null时,显示该项目;
		sb.append(" or (a.project_progress is null and a.project_status=1 ) ");

		//2.当前用户可以看到 自己创建的项目,并且项目的状态为编辑状态
		sb.append(" or (a.create_by = '"+userId+"' and a.project_status=0) ");
		//3.当前用户可以看到 自己主负责的项目,项目状态不能为 个人编辑 状态,此状态还处于材料收集阶段;
		sb.append(" or (a.primary_person='"+userId+"' and a.project_status!=0 ) ");
		//4.当前用户可以看到 自己副负责的(所在项目的副负责人)项目,项目状态不能为 个人编辑 状态,此状态还处于材料收集阶段;
		sb.append(" or (find_in_set('"+userId+"',a.team_members) and a.project_status!=0)");
		//5.当前用户可以看到 自己参与的(所在项目小组)项目,并且项目进度小于5
		sb.append(" or (find_in_set('"+userId+"',a.project_team_members) and a.project_progress<5)");
		//6.当前用户可以看到 按 自己所在角色与项目进度绑定的集合 ,进行筛选
		Set<String> progressSet= UserUtils.getProjectProgressSet(userId);//获取该用户拥有的项目进度;
		if(CollectionUtils.isNotEmpty(progressSet)){
			String progressIn= "'"+Joiner.on("','").skipNulls().join(progressSet)+"'";
			sb.append(" or a.project_progress in ("+progressIn+")");
		}

		sb.append(")");

		projectInfo.getSqlMap().put("dsf",sb.toString());
		//orderBy:1.把自己创建的项目,且处于 个人编辑 状态的,优先显示
		//orderBy:1.1 项目状态处于4(立项会初审)或者5(立项会复审),次优显示
		//orderBy:2.是项目负责人的项目,次优显示
		//orderBy:3.是项目小组的项目,次次优显示
		//orderBy:4.更新时间,最后显示
		String orderBy="(a.create_by = '"+userId+"' and a.project_status=0) DESC,(a.project_status=4 or a.project_status=5) DESC,(a.primary_person='"+userId+"' or find_in_set('"+userId+"',a.team_members)) DESC,(find_in_set('"+userId+"',a.project_team_members)) DESC,a.update_date DESC";
		//page.setOrderBy(orderBy);//该方式会过滤orderby;Page.getOrderBy;不适用本场景
		projectInfo.getSqlMap().put("orderBy",orderBy);
		return super.findPage(page, projectInfo);

	}

	public Page<ProjectInfo> findPage(Page<ProjectInfo> page, ProjectInfo projectInfo) {
		return super.findPage(page, projectInfo);
	}
	
	@Transactional(readOnly = false)
	public void save(ProjectInfo projectInfo) {
		if (projectInfo.getContent()!=null){
			projectInfo.setContent(StringEscapeUtils.unescapeHtml4(projectInfo.getContent()));
		}
		super.save(projectInfo);
	}
	
	@Transactional(readOnly = false)
	public void delete(ProjectInfo projectInfo) {
		super.delete(projectInfo);
		//projectInfoProgressDao.delete(new ProjectInfoProgress(projectInfo));
	}

	/**
	 * 获取更新时间+15天 in (lastExecuteTime,currentExecuteTime]
	 * 筛选出 15天以上未更新的项目,做通知提醒
	 * @param lastExecuteTime
	 * @param currentExecuteTime
	 * @return
	 */
	public List<ProjectInfo> selectProjectInfoUpdateGte15days(Date lastExecuteTime, Date currentExecuteTime){
		ProjectInfo projectInfo=new ProjectInfo();
		projectInfo.setProjectStatus("1");
		//测试时使用的条件
		//projectInfo.getSqlMap().put("dsf"," and date_add(a.update_date, interval 23 hour)> '"+ DateUtils.formatDateTime(lastExecuteTime)+"' and date_add(a.update_date, interval 23 hour) <='"+DateUtils.formatDateTime(currentExecuteTime)+"' ");
		projectInfo.getSqlMap().put("dsf"," and date_add(a.update_date, interval 15 day)> '"+DateUtils.formatDateTime(lastExecuteTime)+"' and date_add(a.update_date, interval 15 day) <='"+DateUtils.formatDateTime(currentExecuteTime)+"' ");
		return super.findList(projectInfo);
	}
	
}