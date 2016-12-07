/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {
	
	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);

	/**
	 * 通过roleId获取用户列表,仅返回用户id，name，login_name
	 * @param roleId
	 * @return
	 */
	public List<User> findUserByRoleId(@Param("roleId") String roleId);


	/**
	 *根据 岗位类型stationType 获取用户列表,仅返回用户id，name，login_name
	 * @param stationType
	 * @return
	 */
	public List<User> findUserByStationType(@Param("stationType") String stationType);

	/**
	 * 根据 项目进度projectProgress 获取用户列表,仅返回用户id，name，login_name
	 * @param projectProgress
	 * @return
	 */
	public List<User> findUserByProjectProgress(@Param("projectProgress") String projectProgress);

	/**
	 * 通过登录名字符串（例如 "ad","tom" ）获取用户列表,仅返回用户id和name，目前用于项目动态
	 * @param loginNames
	 * @return
	 */
	public List<User> findUserByLoginNames(List<String> loginNames);

	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);

	/**
	 * 删除用户岗位关联数据
	 * @param user
	 * @return
	 */
	public int deleteUserStation(User user);

	/**
	 * 插入用户岗位关联数据
	 * @param user
	 * @return
	 */
	public int insertUserStation(User user);
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);

}
