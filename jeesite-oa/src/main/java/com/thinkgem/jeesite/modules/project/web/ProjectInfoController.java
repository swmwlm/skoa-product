/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfoProgress;
import com.thinkgem.jeesite.modules.project.entity.ProjectNote;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoService;
import com.thinkgem.jeesite.modules.project.service.ProjectNoteService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.ProjectInfoUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 项目管理Controller
 * @author evan
 * @version 2016-10-25
 */
@Controller
@RequestMapping(value = "${adminPath}/project/projectInfo")
public class ProjectInfoController extends BaseController {

	@Autowired
	private ProjectInfoService projectInfoService;

	@Autowired
	private SystemService systemService;

	@Autowired
	private ProjectNoteService projectNoteService;

	@ModelAttribute
	public ProjectInfo get(@RequestParam(required=false) String id,Model model) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		ProjectInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = projectInfoService.get(id);
		}
		if (entity == null){
			entity = new ProjectInfo();
		}
		//保存从数据库获取的原始值对象
		ProjectInfo orignProjectInfo= (ProjectInfo) BeanUtils.cloneBean(entity);
		model.addAttribute("orignProjectInfo",orignProjectInfo);
		return entity;
	}
	
	@RequiresPermissions("project:projectInfo:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProjectInfo projectInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProjectInfo> page = projectInfoService.findPageDSF(new Page<ProjectInfo>(request, response), projectInfo);
		model.addAttribute("page", page);
		return "modules/project/projectInfoList";
	}

	@RequiresPermissions("project:projectInfo:view")
	@RequestMapping(value = "form")
	public String form(ProjectInfo projectInfo, Model model,RedirectAttributes redirectAttributes) {
		//校验当前用户是否拥有该项目的编辑权限
		if(StringUtils.isBlank(projectInfo.getId())||ProjectInfoUtils.editableProject(projectInfo)) {
			model.addAttribute("projectInfo", projectInfo);
			return "modules/project/projectInfoForm";
		}
		addMessage(redirectAttributes, "没有权限,编辑项目失败");
		return "redirect:"+Global.getAdminPath()+"/project/projectInfo/?repage";
	}

	@RequiresPermissions("project:projectInfo:view")
	@RequestMapping(value = "save")
	public String save(ProjectInfo projectInfo, Model model, RedirectAttributes redirectAttributes) {
		//获取未修改前的值对象
		ProjectInfo orignProjectInfo= (ProjectInfo) model.asMap().get("orignProjectInfo");
		//校验当前用户是否拥有该项目的编辑权限或者新增项目
		if("".equals(projectInfo.getId())||ProjectInfoUtils.editableProject(orignProjectInfo)) {
			if (!beanValidator(model, projectInfo)) {
				return form(projectInfo, model, redirectAttributes);
			}
			projectInfoService.save(projectInfo);
			addMessage(redirectAttributes, "保存项目成功");
			return "redirect:" + Global.getAdminPath() + "/project/projectInfo/?repage";
		}
		addMessage(redirectAttributes, "没有权限,编辑项目失败");
		return "redirect:"+Global.getAdminPath()+"/project/projectInfo/?repage";
	}

	@RequiresPermissions("project:projectInfo:view")
	@RequestMapping(value = "view")
	public String view(ProjectInfo projectInfo, Model model,RedirectAttributes redirectAttributes) {
		//校验当前用户是否有该项目的浏览权限
		if(ProjectInfoUtils.viewableProject(projectInfo)) {
			model.addAttribute("projectInfo", projectInfo);

			//获取用户列表，用于@用户
			List<User> userList = systemService.findUser(new User());
			JSONArray json = new JSONArray();
			for (User user : userList) {
				JSONObject jo = new JSONObject();
				jo.put("id", user.getLoginName());
				jo.put("name", user.getName());
				json.add(jo);
			}
//			System.out.println(json.toString());
			model.addAttribute("userList", json.toString());

			ProjectNote projectNote = new ProjectNote();
			projectNote.setProjectId(projectInfo.getId());
			List<ProjectNote> projectNoteList = projectNoteService.findList(projectNote);
			model.addAttribute("projectNoteList", projectNoteList);


			return "modules/project/projectInfoView";
		}
		addMessage(redirectAttributes, "没有此项目查看权限,浏览项目失败");
		return "redirect:"+Global.getAdminPath()+"/project/projectInfo/?repage";
	}

	@RequiresPermissions("project:projectInfo:view")
	@RequestMapping(value = "delete")
	public String delete(ProjectInfo projectInfo, RedirectAttributes redirectAttributes) {
		//校验当前用户是否拥有该项目的编辑权限
		if(ProjectInfoUtils.editableProject(projectInfo)) {
			projectInfoService.delete(projectInfo);
			addMessage(redirectAttributes, "删除项目成功");
			return "redirect:" + Global.getAdminPath() + "/project/projectInfo/?repage";
		}
		addMessage(redirectAttributes, "没有权限,删除项目失败");
		return "redirect:"+Global.getAdminPath()+"/project/projectInfo/?repage";
	}

	@RequiresPermissions("project:projectInfo:view")
	@RequestMapping(value = "updateProgress")
	public String updateProgress(String projectInfoId,String projectProgress,String filepath,String remarks, RedirectAttributes redirectAttributes) {
		if (StringUtils.isAnyBlank(projectInfoId, projectProgress)) {
			addMessage(redirectAttributes, "参数有误,项目进度更新失败!");
			return "redirect:" + Global.getAdminPath() + "/project/projectInfo/?repage";
		}
		ProjectInfo projectInfo = projectInfoService.get(projectInfoId);

		if(null==projectInfo){
			addMessage(redirectAttributes, "参数有误,项目进度更新失败!");
			return "redirect:" + Global.getAdminPath() + "/project/projectInfo/?repage";
		}
		if(projectProgress.equals(projectInfo.getProjectProgress())){
			addMessage(redirectAttributes, "项目进度无变更,更新失败!");
			return "redirect:" + Global.getAdminPath() + "/project/projectInfo/?repage";
		}

		ProjectInfoProgress projectInfoProgress=new ProjectInfoProgress();
		projectInfoProgress.setId("");
		projectInfoProgress.setFilepath(filepath);
		projectInfoProgress.setRemarks(remarks);
		projectInfoProgress.setStatusOrigin(projectInfo.getProjectProgress());
		projectInfoProgress.setStatusCurrent(projectProgress);
		projectInfo.setProjectProgress(projectProgress);

		projectInfo.getProjectInfoProgressList().add(projectInfoProgress);

		projectInfoService.save(projectInfo);
		addMessage(redirectAttributes, "项目进度更新成功!");
		return "redirect:" + Global.getAdminPath() + "/project/projectInfo/?repage";

	}

}