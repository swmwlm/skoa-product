package com.thinkgem.jeesite.restful.module;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AppOaNotify {

    private String type;		// 类型

    private String title;		// 标题
    private String content;		// 内容

    private String readFlag;	// 本人阅读状态

    private String createByName;	// 创建人姓名
    private String createByPhoto;	// 创建人头像
    private Date createDate;	    // 创建日期

    private String projectInfoId;	// 项目id

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getCreateByPhoto() {
        return createByPhoto;
    }

    public void setCreateByPhoto(String createByPhoto) {
        this.createByPhoto = createByPhoto;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getProjectInfoId() {
        return projectInfoId;
    }

    public void setProjectInfoId(String projectInfoId) {
        this.projectInfoId = projectInfoId;
    }
}
