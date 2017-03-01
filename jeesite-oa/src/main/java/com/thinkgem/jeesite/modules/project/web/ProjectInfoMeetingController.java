/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfoMeeting;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoMeetingService;

/**
 * 项目立项审批Controller
 * @author lhy
 * @version 2017-03-01
 */
@Controller
@RequestMapping(value = "${adminPath}/project/projectInfoMeeting")
public class ProjectInfoMeetingController extends BaseController {

	@Autowired
	private ProjectInfoMeetingService projectInfoMeetingService;
	
	@ModelAttribute
	public ProjectInfoMeeting get(@RequestParam(required=false) String id) {
		ProjectInfoMeeting entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = projectInfoMeetingService.get(id);
		}
		if (entity == null){
			entity = new ProjectInfoMeeting();
		}
		return entity;
	}
	
	@RequiresPermissions("project:projectInfoMeeting:view")
	@RequestMapping(value = {"list", ""})
	public String list(ProjectInfoMeeting projectInfoMeeting, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ProjectInfoMeeting> page = projectInfoMeetingService.findPage(new Page<ProjectInfoMeeting>(request, response), projectInfoMeeting); 
		model.addAttribute("page", page);
		return "modules/project/projectInfoMeetingList";
	}

	@RequiresPermissions("project:projectInfoMeeting:view")
	@RequestMapping(value = "form")
	public String form(ProjectInfoMeeting projectInfoMeeting, Model model) {
		model.addAttribute("projectInfoMeeting", projectInfoMeeting);
		return "modules/project/projectInfoMeetingForm";
	}

	@RequiresPermissions("project:projectInfoMeeting:edit")
	@RequestMapping(value = "save")
	public String save(ProjectInfoMeeting projectInfoMeeting, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, projectInfoMeeting)){
			return form(projectInfoMeeting, model);
		}
		projectInfoMeetingService.save(projectInfoMeeting);
		addMessage(redirectAttributes, "保存项目立项审批成功");
		return "redirect:"+Global.getAdminPath()+"/project/projectInfoMeeting/?repage";
	}
	
	@RequiresPermissions("project:projectInfoMeeting:edit")
	@RequestMapping(value = "delete")
	public String delete(ProjectInfoMeeting projectInfoMeeting, RedirectAttributes redirectAttributes) {
		projectInfoMeetingService.delete(projectInfoMeeting);
		addMessage(redirectAttributes, "删除项目立项审批成功");
		return "redirect:"+Global.getAdminPath()+"/project/projectInfoMeeting/?repage";
	}

}