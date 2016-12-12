package com.thinkgem.jeesite.restful.module;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AppSearchUser {

    private String id;      //用户id
    private String name;    //姓名
    private String quanpin;
    private String jianpin;
    private String loginName;//登录名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuanpin() {
        return quanpin;
    }

    public void setQuanpin(String quanpin) {
        this.quanpin = quanpin;
    }

    public String getJianpin() {
        return jianpin;
    }

    public void setJianpin(String jianpin) {
        this.jianpin = jianpin;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
