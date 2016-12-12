package com.thinkgem.jeesite.restful.module;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AppProjectInfo {

    private String id;                     //项目id
    private String projectName;            // 项目名称
    private String projectProgress;        // 项目进度
    private String projectProgressName;    // 项目进度名称
    private String createByUserName;       // 创建者
    private String primaryPersonName;      // 项目负责人
    private String teamMemberNames;        // 项目副负责人
    private String industryDomainName;     // 行业领域
    private Date updateDate;               // 更新日期
    private String updateByName;               // 更新人

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(String projectProgress) {
        this.projectProgress = projectProgress;
    }

    public String getCreateByUserName() {
        return createByUserName;
    }

    public void setCreateByUserName(String createByUserName) {
        this.createByUserName = createByUserName;
    }

    public String getPrimaryPersonName() {
        return primaryPersonName;
    }

    public void setPrimaryPersonName(String primaryPersonName) {
        this.primaryPersonName = primaryPersonName;
    }

    public String getTeamMemberNames() {
        return teamMemberNames;
    }

    public void setTeamMemberNames(String teamMemberNames) {
        this.teamMemberNames = teamMemberNames;
    }

    public String getIndustryDomainName() {
        return industryDomainName;
    }

    public void setIndustryDomainName(String industryDomainName) {
        this.industryDomainName = industryDomainName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getProjectProgressName() {
        return projectProgressName;
    }

    public void setProjectProgressName(String projectProgressName) {
        this.projectProgressName = projectProgressName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }
}
