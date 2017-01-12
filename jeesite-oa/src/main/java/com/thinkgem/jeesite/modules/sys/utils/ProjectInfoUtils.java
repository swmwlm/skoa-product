package com.thinkgem.jeesite.modules.sys.utils;

import com.google.common.base.Splitter;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;

import java.util.List;
import java.util.Set;

/**
 * 关于项目的工具类.
 */
public class ProjectInfoUtils {
	/**
	 * 判断当前用户是否是某个项目的负责人;副负责人也算
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoPrimaryPerson(ProjectInfo projectInfo){
		if(null==projectInfo||null==projectInfo.getPrimaryPerson()||StringUtils.isBlank(UserUtils.getUser().getId()))
			return false;

		//主负责人及副负责人拼装集合;判断当前用户是否属于此集合成员之一;
		String teamMembers=projectInfo.getTeamMembers();
		String primaryPersonId=projectInfo.getPrimaryPerson().getId();
		String strIds=primaryPersonId+","+teamMembers;
		List<String> strIdList=Splitter.on(',').trimResults().omitEmptyStrings().splitToList(strIds);
		return strIdList.contains(UserUtils.getUser().getId());
	}

	/**
	 * 判断当前用户是否是某个项目的创建者
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoCreator(ProjectInfo projectInfo){
		if(null==projectInfo||null==projectInfo.getCreateBy())
			return false;
		return UserUtils.getUser().getId().equals(projectInfo.getCreateBy().getId());
	}

	/**
	 * 判断当前用户是否是某个项目的创建者 (提供jstl fn自定义函数接口)
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoCreator(Object projectInfo){
		ProjectInfo _projectInfo=null;
		if(projectInfo instanceof  ProjectInfo) {
			_projectInfo= (ProjectInfo) projectInfo;
			return ProjectInfoUtils.isProjectInfoCreator(_projectInfo);
		}
		return false;
	}

	/**
	 * 判断当前用户是否可以编辑或删除该项目
	 * @param projectInfo
	 * @return
	 */
	public static Boolean editableProject(ProjectInfo projectInfo) {
		//1.如果当前用户是项目的创建者,且该项目的状态为 个人编辑 时,则可以编辑该项目
		if(ProjectInfoUtils.isProjectInfoCreatorAndProjectStatus0(projectInfo))
			return true;
		//2.如果当前用户是项目的负责人,且项目状态不能处于0(个人编辑),4(申请上立项会初审)或者5(申请上立项会复审)状态,可以编辑项目
		//也即是:当前用户是项目的负责人,且项目状态为1(项目发布),2(项目暂停),3(项目完成)时,拥有编辑项目权限;
		if(ProjectInfoUtils.isProjectInfoPrimaryPerson(projectInfo)
				&&!"0".equals(projectInfo.getProjectStatus())
				&&!"4".equals(projectInfo.getProjectStatus())
				&&!"5".equals(projectInfo.getProjectStatus()))
			return true;
		return false;
	}

	/**
	 * 判断是否为项目创建者且项目状态为0(个人编辑);
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoCreatorAndProjectStatus0(ProjectInfo projectInfo){
		return ProjectInfoUtils.isProjectInfoCreator(projectInfo)&&StringUtils.equals("0",projectInfo.getProjectStatus());
	}
	/**
	 * 判断当前用户是否可以编辑或删除该项目 (提供jstl fn自定义函数接口)
	 * @param projectInfo
	 * @return
	 */
	public static Boolean editableProject(Object projectInfo) {
		ProjectInfo _projectInfo=null;
		if(projectInfo instanceof  ProjectInfo) {
			_projectInfo= (ProjectInfo) projectInfo;
			return ProjectInfoUtils.editableProject(_projectInfo);
		}
		return false;
	}

	/**
	 * 判断当前用户是否新增项目(提供jstl fn自定义函数接口)
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoNew(Object projectInfo){
		if(projectInfo!=null){
			ProjectInfo _projectInfo=null;
			if(projectInfo instanceof  ProjectInfo) {
				_projectInfo= (ProjectInfo) projectInfo;

				return StringUtils.isBlank(_projectInfo.getId());
			}
		}
		return true;
	}

	/**
	 * 判断当前用户是否为项目负责人(提供jstl fn自定义函数接口)
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoPrimaryPerson(Object projectInfo){
		ProjectInfo _projectInfo=null;
		if(projectInfo instanceof  ProjectInfo) {
			_projectInfo= (ProjectInfo) projectInfo;
			return ProjectInfoUtils.isProjectInfoPrimaryPerson(_projectInfo);
		}
		return false;
	}

	/**
	 * 判断当前用户是否允许变更项目进度;项目处于采编阶段,是不允许更新项目进度的
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isAllowedUpdateProjectProgress(Object projectInfo) {
		ProjectInfo _projectInfo = null;
		if (projectInfo instanceof ProjectInfo) {
			_projectInfo = (ProjectInfo) projectInfo;
			return ProjectInfoUtils.isProjectInfoPrimaryPerson(_projectInfo)
					&& !StringUtils.equals("0", ((ProjectInfo) projectInfo).getProjectStatus())
					&& !StringUtils.equals("4", ((ProjectInfo) projectInfo).getProjectStatus())
					&& !StringUtils.equals("5", ((ProjectInfo) projectInfo).getProjectStatus());
		}
		return false;
	}


	/**
	 * 判断当前用户是否允许立项审批或者复审
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isAllowedUpdateProjectMeeting(Object projectInfo) {
		try {
			ProjectInfo _projectInfo = null;
			if (projectInfo instanceof ProjectInfo) {
				_projectInfo = (ProjectInfo) projectInfo;
				return ProjectInfoUtils.isProjectMeetingForOne(_projectInfo) || ProjectInfoUtils.isProjectMeetingForSecond(_projectInfo);
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}


	/**
	 * 是否是项目小组成员
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoTeam(ProjectInfo projectInfo){
		if(StringUtils.isBlank(projectInfo.getProjectTeamMembers()))
			return false;

		String strProjectTeamMembers=","+projectInfo.getProjectTeamMembers()+",";
		String strUserId= ","+UserUtils.getUser().getId()+",";
		return strProjectTeamMembers.indexOf(strUserId)>-1;
	}

	/**
	 * 判断当前用户是否为项目小组成员(提供jstl fn自定义函数接口)
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoTeam(Object projectInfo){
		ProjectInfo _projectInfo=null;
		if(projectInfo instanceof  ProjectInfo) {
			_projectInfo= (ProjectInfo) projectInfo;
			return ProjectInfoUtils.isProjectInfoTeam(_projectInfo);
		}
		return false;
	}

	/**
	 * 判断当前用户 是否可以浏览该项目
	 * @param projectInfo
	 * @return
	 */
	public static Boolean viewableProject(ProjectInfo projectInfo) {
		//1.判断当前用户是否有@RequiresPermissions("project:projectInfo:view")权限;
		//1.2 当项目进度为null,且项目状态为4,且当前用户是项目所属部门的部门负责人,可以查看;
		if(null==projectInfo.getProjectProgress()&&ProjectInfoUtils.isProjectMeetingForOne(projectInfo))
			return true;
		//1.3 当项目进度为null,且项目状态为5时,且当前用户拥有合伙人角色时,可以查看;
		if(null==projectInfo.getProjectProgress()&&ProjectInfoUtils.isProjectMeetingForSecond(projectInfo))
			return true;
		//1.4 项目状态为 1发布状态 下,当项目进度为null时,显示该项目;
		if(null==projectInfo.getProjectProgress()&&StringUtils.equals("1",projectInfo.getProjectStatus()))
			return true;

		//2.当前用户可以看到项目进度为0或者1的项目,且项目状态不为 个人编辑 状态的
		if(null!=projectInfo.getProjectProgress()&&Integer.parseInt(projectInfo.getProjectProgress())<2&&!StringUtils.equals("0",projectInfo.getProjectStatus()))
			return true;
		//2.当前用户可以看到 自己创建的项目,并且项目的状态为编辑状态
		if(ProjectInfoUtils.isProjectInfoCreator(projectInfo)&&StringUtils.equals("0",projectInfo.getProjectStatus()))
			return true;
		//3.当前用户可以看到 自己负责(包含副负责人)的项目,项目状态不能为 个人编辑 状态,此状态还处于材料收集阶段;
		if(ProjectInfoUtils.isProjectInfoPrimaryPerson(projectInfo)&&!StringUtils.equals("0",projectInfo.getProjectStatus()))
			return true;
		//4.当前用户可以看到 自己参与的(所在项目小组)项目,并且项目进度小于5
		if(ProjectInfoUtils.isProjectInfoTeam(projectInfo)&&Integer.valueOf(projectInfo.getProjectProgress())<5)
			return true;
		//5.当前项目进度在当前用户所拥有的项目进度集合中的,表示可以浏览
		Set<String> projectProgressSet=UserUtils.getProjectProgressSet();
		if(Collections3.isEmpty(projectProgressSet))
			return false;

		return projectProgressSet.contains(projectInfo.getProjectProgress());
	}

	/**
	 * 判断当前用户 是否拥有 4申请上立项会初审 权限
	 * 项目所属的部门的主副负责人 是当前用户,则表示有权限
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectMeetingForOne(ProjectInfo projectInfo){
		String userId=UserUtils.getUser().getId();
		String primaryPersonOffice=projectInfo.getOffice().getPrimaryPerson().getId();
		String deputyPersonOffice=projectInfo.getOffice().getDeputyPerson().getId();
		return StringUtils.equals("4",projectInfo.getProjectStatus())&&(StringUtils.equals(userId,primaryPersonOffice)||StringUtils.equals(userId,deputyPersonOffice));
	}

	/**
	 * 判断当前用户是否拥有 5申请上立项会复审 权限
	 * 当前用户 拥有 合伙人角色权限,表示有权限
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectMeetingForSecond(ProjectInfo projectInfo){
		return StringUtils.equals("5",projectInfo.getProjectStatus())&&UserUtils.isPartnerRole();
	}
}
