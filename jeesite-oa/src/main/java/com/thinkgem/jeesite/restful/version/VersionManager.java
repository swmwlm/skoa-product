package com.thinkgem.jeesite.restful.version;

import com.thinkgem.jeesite.common.utils.StringUtils;

import java.io.IOException;

/**
 * 检查当前版本
 * 定义一个私有的内部类，在第一次用这个嵌套类时，会创建一个实例。而类型为SingletonHolder的类，只有在Singleton.getInstance()中调用，由于私有的属性，他人无法使用SingleHolder，不调用Singleton.getInstance()就不会创建实例。达到了lazy loading的效果，即按需创建实例.
 * Created by shouke on 2017/1/23.
 */
public class VersionManager {
	private VersionProperty property; //文件配置
	private static String propertyName = "/version.xml"; //默认的配置文件

	public VersionManager() {
		this(VersionManager.propertyName);
	}


	public VersionManager(String propertyName) {
		try {
			property = new VersionProperty(VersionManager.class.getResource(propertyName).getPath());
		} catch (IOException e) {
			throw new RuntimeException(propertyName + " can not found", e);
		}
	}

	public static VersionManager getInstance() {
		return VersionManagerHolder.instance;
	}

	private static class VersionManagerHolder{
		private final static VersionManager instance=new VersionManager();
	}

	/**
	 * 检查版本*
	 * @param version 版本号
	 * @param clientType 终端类型
	 * @return 当前最新版本
	 */
	public Version check(String version, String clientType) {
		if (property == null || StringUtils.isEmpty(version) || StringUtils.isEmpty(clientType)) {
			return null;
		}

		//根据当前客户端类型获取当前的版本
		Version nowVersion = property.getNowVersion(ClientType.getClientType(clientType));

		//版本一致 则返回null,表示不需要更新;
		if (nowVersion == null || version.equalsIgnoreCase(nowVersion.getVersion())) {
			return null;
		}

		//版本不一致 返回设置的最新版本
		return nowVersion;
	}



}
