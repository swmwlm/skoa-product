/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.utils;

import com.google.common.base.Splitter;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.sys.dao.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.thinkgem.jeesite.common.utils.Collections3.extractToList;

/**
 * 用户工具类
 * @author ThinkGem
 * @version 2013-12-05
 */
public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static StationDao stationDao = SpringContextHolder.getBean(StationDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_STATION_LIST = "stationList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

	private static DictDao dictDao=SpringContextHolder.getBean(DictDao.class);
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
		if (user ==  null){
			user = userDao.get(id);
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			user.setStationList(stationDao.findList(new Station(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName){
		User user = (User)CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
		if (user == null){
			user = userDao.getByLoginName(new User(null, loginName));
			if (user == null){
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
		}
		return user;
	}
	
	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache(){
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		UserUtils.clearCache(getUser());
	}
	
	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user){
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
		CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
		if (user.getOffice() != null && user.getOffice().getId() != null){
			CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ + user.getOffice().getId());
		}
	}
	
	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<Role> getRoleList(){
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>)getCache(CACHE_ROLE_LIST);
		if (roleList == null){
			User user = getUser();
			if (user.isAdmin()){
				roleList = roleDao.findAllList(new Role());
			}else{
				Role role = new Role();
				role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
				roleList = roleDao.findList(role);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}

	/**
	 * 判断当前角色集合中是否存在
	 * @return
	 */
	public static Boolean isPartnerRole() {
		return isPartnerRole(UserUtils.getUser().getId());
	}

	/**
	 * 判断某个用户角色集合中是否存在
	 * @param userId
	 * @return
	 */
	public static Boolean isPartnerRole(String userId) {
		User user = get(userId);
		List<String> roleNameList = Collections3.extractToList(user.getRoleList(), "name");
		return roleNameList.contains("合伙人");
	}

	/**
	 * 获取所有岗位列表
	 * @return
	 */
	public static List<Station> getStationList(){
//		@SuppressWarnings("unchecked")
//		List<Station> stationList = (List<Station>)getCache(CACHE_STATION_LIST);
//		if (stationList == null){
//			User user = getUser();
//			if (user.isAdmin()){
//				stationList = stationDao.findAllList(new Station());
//			}else{
//				Station station = new Station();
//				station.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
//				stationList = stationDao.findList(station);
//			}
//			putCache(CACHE_STATION_LIST, stationList);
//		}
//		return stationList;

		//暂时不做缓存
		List<Station> stationList = stationDao.findAllList(new Station());
		return stationList;
	}
	
	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList(){
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>)getCache(CACHE_MENU_LIST);
		if (menuList == null){
			User user = getUser();
			if (user.isAdmin()){
				menuList = menuDao.findAllList(new Menu());
			}else{
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}
	
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList(){
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>)getCache(CACHE_AREA_LIST);
		if (areaList == null){
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}
	
	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_LIST);
		if (officeList == null){
			User user = getUser();
			if (user.isAdmin()){
				officeList = officeDao.findAllList(new Office());
			}else{
				Office office = new Office();
				office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
				officeList = officeDao.findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeAllList(){
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>)getCache(CACHE_OFFICE_ALL_LIST);
		if (officeList == null){
			officeList = officeDao.findAllList(new Office());
		}
		return officeList;
	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {
			
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
//			subject.logout();
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	// ============== User Cache ==============
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
//		Object obj = getCacheMap().get(key);
		Object obj = getSession().getAttribute(key);
		return obj==null?defaultValue:obj;
	}

	public static void putCache(String key, Object value) {
//		getCacheMap().put(key, value);
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
//		getCacheMap().remove(key);
		getSession().removeAttribute(key);
	}

	public static void setJflowNo(String currentUserName){
		getSession().setAttribute("No",currentUserName);
	}
//	public static Map<String, Object> getCacheMap(){
//		Principal principal = getPrincipal();
//		if(principal!=null){
//			return principal.getCacheMap();
//		}
//		return new HashMap<String, Object>();
//	}

	/**
	 * 获取当前用户拥有的项目进度set集合
	 * @return
	 */
	public static Set<String> getProjectProgressSet(){
		User user = getUser();
		List<Role> roleList=user.getRoleList();
		if(CollectionUtils.isEmpty(roleList))
			return null;

		List<String> roleIdList= extractToList(roleList,"id");
		List<Dict> dictList=dictDao.findListByRoleIdList(roleIdList);
		if(CollectionUtils.isEmpty(dictList))
			return null;

		List<String> projectProgressList=Collections3.extractToList(dictList,"value");
		Set<String> projectProgressSet=new HashSet<>(projectProgressList);
		return projectProgressSet;
	}

	/**
	 * 获取某个用户拥有的项目进度set集合
	 * @param userId
	 * @return
	 */
	public static Set<String> getProjectProgressSet(String userId){
		User user = get(userId);
		List<Role> roleList=user.getRoleList();
		if(CollectionUtils.isEmpty(roleList))
			return null;

		List<String> roleIdList= extractToList(roleList,"id");
		List<Dict> dictList=dictDao.findListByRoleIdList(roleIdList);
		if(CollectionUtils.isEmpty(dictList))
			return null;

		List<String> projectProgressList=Collections3.extractToList(dictList,"value");
		Set<String> projectProgressSet=new HashSet<>(projectProgressList);
		return projectProgressSet;
	}

	/**
	 * 获取消息通知的人员列表
	 * （目前用于：项目进度更新，项目动态添加）
	 * @param projectInfo  项目
	 * @param atUserIds    At通知的人（多个逗号分割）
	 * @param createUserId 创建人Id
	 * @return
	 */
	public static Set<String> getNotifyUserIds(ProjectInfo projectInfo, String atUserIds, String createUserId) {
		Set<String> resultSet = new HashSet<>();
		//1，获取负责人，副负责人
		if (projectInfo.getPrimaryPerson() != null) {
			resultSet.add(projectInfo.getPrimaryPerson().getId());
		}
		if (projectInfo.getTeamMembers() != null) {
			List<String> teamMembers = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(projectInfo.getTeamMembers());
			resultSet.addAll(teamMembers);
		}
		//1.2，获取项目小组成员,且项目进度<5的成员
		if (projectInfo.getProjectTeamMembers() != null&&projectInfo.getProjectProgress()!=null&&Integer.valueOf(projectInfo.getProjectProgress())<5) {
			List<String> projectTeamMembers = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(projectInfo.getProjectTeamMembers());
			resultSet.addAll(projectTeamMembers);
		}
		//2，获取高层人
		List<User> userList2 = userDao.findUserByStationType("1");
		if (CollectionUtils.isNotEmpty(userList2)) {
			resultSet.addAll(Collections3.extractToList(userList2, "id"));
		}
		//3，获取有阶段角色的（30%-100%）
		if (StringUtils.isNotBlank(projectInfo.getProjectProgress())) {
			if (Integer.valueOf(projectInfo.getProjectProgress()) > 1) {
				List<User> userList3 = userDao.findUserByProjectProgress(projectInfo.getProjectProgress());
				if (CollectionUtils.isNotEmpty(userList3)) {
					resultSet.addAll(Collections3.extractToList(userList3, "id"));
				}
			}
		}
		//4，atUserIds添加
		if (StringUtils.isNotBlank(atUserIds)) {
			List<String> atUserIdList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(atUserIds);
			resultSet.addAll(atUserIdList);
		}
		//5，去掉创建人; 去除重复的人(set自动去除了)
		if (resultSet.contains(createUserId)) {
			resultSet.remove(createUserId);
		}
		return resultSet;
	}

	public static String getNotifyUserIdsString(ProjectInfo projectInfo, String atUserIds, String createUserId) {
		Set<String> stringSet = getNotifyUserIds(projectInfo, atUserIds, createUserId);
		return Collections3.convertToString(stringSet, ",");
	}


}
