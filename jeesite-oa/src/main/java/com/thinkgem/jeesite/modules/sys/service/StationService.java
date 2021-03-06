/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Station;
import com.thinkgem.jeesite.modules.sys.dao.StationDao;

/**
 * 岗位管理Service
 * @author lhy
 * @version 2016-10-19
 */
@Service
@Transactional(readOnly = true)
public class StationService extends TreeService<StationDao, Station> {

	public Station get(String id) {
		return super.get(id);
	}
	
	public List<Station> findList(Station station) {
		if (StringUtils.isNotBlank(station.getParentIds())){
			station.setParentIds(","+station.getParentIds()+",");
		}
		return super.findList(station);
	}
	
	@Transactional(readOnly = false)
	public void save(Station station) {
		super.save(station);
	}
	
	@Transactional(readOnly = false)
	public void delete(Station station) {
		super.delete(station);
	}
	
}