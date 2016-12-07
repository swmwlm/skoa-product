/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.batchlog.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.batchlog.dao.SysBatchlogDao;
import com.thinkgem.jeesite.modules.batchlog.entity.SysBatchlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 任务日志Service
 * @author evan
 * @version 2016-12-07
 */
@Service
@Transactional(readOnly = true)
public class SysBatchlogService extends CrudService<SysBatchlogDao, SysBatchlog> {

	@Autowired
	private SysBatchlogDao sysBatchlogDao;

	public SysBatchlog get(String id) {
		return super.get(id);
	}
	
	public List<SysBatchlog> findList(SysBatchlog sysBatchlog) {
		return super.findList(sysBatchlog);
	}
	
	public Page<SysBatchlog> findPage(Page<SysBatchlog> page, SysBatchlog sysBatchlog) {
		return super.findPage(page, sysBatchlog);
	}
	
	@Transactional(readOnly = false)
	public void save(SysBatchlog sysBatchlog) {
		super.save(sysBatchlog);
	}
	
	@Transactional(readOnly = false)
	public void delete(SysBatchlog sysBatchlog) {
		super.delete(sysBatchlog);
	}

	public Date selectLastExecuteTimeByJobName(String jobName){
		return sysBatchlogDao.selectLastExecuteTimeByJobName(jobName);
	}
	
}