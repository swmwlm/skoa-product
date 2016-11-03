package com.thinkgem.jeesite.modules.sys.utils;

import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;

import java.util.Set;

/**
 * 关于项目的工具类.
 */
public class ProjectInfoUtils {
	/**
	 * 判断当前用户是否是某个项目的负责人
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoPrimaryPerson(ProjectInfo projectInfo){
		if(null==projectInfo||null==projectInfo.getPrimaryPerson())
			return false;
		return UserUtils.getUser().getId().equals(projectInfo.getPrimaryPerson().getId());
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
	 * 判断当前用户是否可以编辑或删除该项目
	 * @param projectInfo
	 * @return
	 */
	public static Boolean editableProject(ProjectInfo projectInfo) {
		//1.如果当前用户是项目的创建者,并且该项目的状态为推介人编辑时,则可以编辑该项目
		if(ProjectInfoUtils.isProjectInfoCreator(projectInfo)&&"0".equals(projectInfo.getProjectStatus()))
			return true;

		//2.如果当前用户是项目的负责人,则可以编辑该项目
		if(ProjectInfoUtils.isProjectInfoPrimaryPerson(projectInfo))
			return true;
		return false;
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
	 * 是否是项目小组成员
	 * @param projectInfo
	 * @return
	 */
	public static Boolean isProjectInfoTeam(ProjectInfo projectInfo){
		if(StringUtils.isBlank(projectInfo.getTeamMembers()))
			return false;

		String strTeamMembers=","+projectInfo.getTeamMembers()+",";
		String strUserId= ","+UserUtils.getUser().getId()+",";
		return strTeamMembers.indexOf(strUserId)>-1;
	}

	/**
	 * 判断当前用户 是否可以浏览该项目
	 * @param projectInfo
	 * @return
	 */
	public static Boolean viewableProject(ProjectInfo projectInfo) {
		//1.判断当前用户是否有@RequiresPermissions("project:projectInfo:view")权限;
		//2.判断当前用户是否为项目负责人,若是,则可以浏览
		if(ProjectInfoUtils.isProjectInfoPrimaryPerson(projectInfo))
			return true;

		//3.判断 当前项目进度是否为0或者1,若是,则可以浏览
		if(StringUtils.equals("0",projectInfo.getProjectProgress())||StringUtils.equals("1",projectInfo.getProjectProgress()))
			return true;

		//4.若项目进度 为空,判断当前用户是否为项目创建者,若是,表示可以浏览
		if(projectInfo.getProjectProgress()==null&&ProjectInfoUtils.isProjectInfoCreator(projectInfo))
			return true;

		//5.当前用户可以看到 自己参与的(所在项目小组)项目,并且项目进度小于5
		if(ProjectInfoUtils.isProjectInfoTeam(projectInfo)&&Integer.valueOf(projectInfo.getProjectProgress())<5)
			return true;

		//6.当前项目进度在当前用户所拥有的项目进度集合中的,表示可以浏览
		Set<String> projectProgressSet=UserUtils.getProjectProgressSet();
		if(Collections3.isEmpty(projectProgressSet))
			return false;

		return projectProgressSet.contains(projectInfo.getProjectProgress());
	}
}
