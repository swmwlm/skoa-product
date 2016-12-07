/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.batchlog.web;

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
import com.thinkgem.jeesite.modules.batchlog.entity.SysBatchlog;
import com.thinkgem.jeesite.modules.batchlog.service.SysBatchlogService;

/**
 * 任务日志Controller
 * @author evan
 * @version 2016-12-07
 */
@Controller
@RequestMapping(value = "${adminPath}/batchlog/sysBatchlog")
public class SysBatchlogController extends BaseController {

	@Autowired
	private SysBatchlogService sysBatchlogService;
	
	@ModelAttribute
	public SysBatchlog get(@RequestParam(required=false) String id) {
		SysBatchlog entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = sysBatchlogService.get(id);
		}
		if (entity == null){
			entity = new SysBatchlog();
		}
		return entity;
	}
	
	@RequiresPermissions("batchlog:sysBatchlog:view")
	@RequestMapping(value = {"list", ""})
	public String list(SysBatchlog sysBatchlog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysBatchlog> page = sysBatchlogService.findPage(new Page<SysBatchlog>(request, response), sysBatchlog); 
		model.addAttribute("page", page);
		return "modules/batchlog/sysBatchlogList";
	}

	@RequiresPermissions("batchlog:sysBatchlog:view")
	@RequestMapping(value = "form")
	public String form(SysBatchlog sysBatchlog, Model model) {
		model.addAttribute("sysBatchlog", sysBatchlog);
		return "modules/batchlog/sysBatchlogForm";
	}

	@RequiresPermissions("batchlog:sysBatchlog:edit")
	@RequestMapping(value = "save")
	public String save(SysBatchlog sysBatchlog, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysBatchlog)){
			return form(sysBatchlog, model);
		}
		sysBatchlogService.save(sysBatchlog);
		addMessage(redirectAttributes, "保存任务日志成功");
		return "redirect:"+Global.getAdminPath()+"/batchlog/sysBatchlog/?repage";
	}
	
	@RequiresPermissions("batchlog:sysBatchlog:edit")
	@RequestMapping(value = "delete")
	public String delete(SysBatchlog sysBatchlog, RedirectAttributes redirectAttributes) {
		sysBatchlogService.delete(sysBatchlog);
		addMessage(redirectAttributes, "删除任务日志成功");
		return "redirect:"+Global.getAdminPath()+"/batchlog/sysBatchlog/?repage";
	}

}