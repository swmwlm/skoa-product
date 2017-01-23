package com.thinkgem.jeesite.restful.version;

import com.thinkgem.jeesite.common.utils.StringUtils;

import java.io.Serializable;

/**
 * 版本类定义
 * Created by shouke on 2017/1/23.
 */
public class Version  implements Serializable {
	private ClientType type; //终端类型
	private String version; //版本号
	private String url; //新版本下载地址
	private String message; //更新内容

	/**
	 * 检查值是否符合要求*
	 * @param type 终端类型值
	 */
	public static final boolean checkType(String type) {
		if (StringUtils.isEmpty(type)) {
			return false;
		}

		type = type.trim().toLowerCase();
		return "android".equals(type) || "iphone".equals(type);
	}

	public ClientType getType() {
		return type;
	}

	public void setType(ClientType type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
