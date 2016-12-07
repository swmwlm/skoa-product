/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.batchlog.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.batchlog.entity.SysBatchlog;

import java.util.Date;

/**
 * 任务日志DAO接口
 * @author evan
 * @version 2016-12-07
 */
@MyBatisDao
public interface SysBatchlogDao extends CrudDao<SysBatchlog> {
	/**
	 * 获取某个任务最近的执行时间
	 * @param jobName
	 * @return
	 */
	Date selectLastExecuteTimeByJobName(String jobName);
}