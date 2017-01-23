package com.thinkgem.jeesite.restful.version;

import com.thinkgem.jeesite.common.utils.StringUtils;

import java.io.IOException;

/**
 * 检查当前版本
 * Created by shouke on 2017/1/23.
 */
public class VersionManager {
	private static VersionManager me=new VersionManager();
	private VersionProperty property; //文件配置
	private static String propertyName = "/version.xml"; //默认的配置文件

	public VersionManager() {
		this(propertyName);
	}

	public VersionManager(String propertyName) {
		try {
			property = new VersionProperty(VersionManager.class.getResource(propertyName).getPath());
		} catch (IOException e) {
			throw new RuntimeException(propertyName + " can not found", e);
		}
	}

	public static VersionManager me() {
		return me;
	}

	/**
	 * 检查版本*
	 * @param version 版本号
	 * @param client 终端类型
	 * @return 当前最新版本
	 */
	public Version check(String version, String client) {
		if (property == null || StringUtils.isEmpty(version) || StringUtils.isEmpty(client)) {
			return null;
		}

		//根据当前客户端类型获取当前的版本
		Version nowVersion = property.getNowVersion(ClientType.getClientType(client));

		//版本一致 则返回null,表示不需要更新;
		if (nowVersion == null || version.equalsIgnoreCase(nowVersion.getVersion())) {
			return null;
		}

		//版本不一致 返回设置的最新版本
		return nowVersion;
	}

}
