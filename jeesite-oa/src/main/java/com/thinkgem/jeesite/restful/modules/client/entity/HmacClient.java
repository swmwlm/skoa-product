/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.restful.modules.client.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * restfulEntity
 * @author evan
 * @version 2016-11-08
 */
public class HmacClient extends DataEntity<HmacClient> {
	
	private static final long serialVersionUID = 1L;
	private String clientName;		// 客户端名称
	private String clientId;		// client_id
	private String clientSecret;		// client_secret
	private String clientType;//客户端类型
	private Boolean regernate=false;  //是否重新生成appId,appKey

	public HmacClient() {
		super();
	}

	public HmacClient(String id){
		super(id);
	}

	@Length(min=0, max=200, message="客户端名称长度必须介于 0 和 200 之间")
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	@Length(min=0, max=200, message="client_id长度必须介于 0 和 200 之间")
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	@Length(min=0, max=200, message="client_secret长度必须介于 0 和 200 之间")
	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public Boolean getRegernate() {
		return regernate;
	}

	public void setRegernate(Boolean regernate) {
		this.regernate = regernate;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
}