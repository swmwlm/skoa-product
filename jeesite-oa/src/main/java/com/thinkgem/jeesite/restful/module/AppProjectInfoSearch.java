package com.thinkgem.jeesite.restful.module;




public class AppProjectInfoSearch {

    private String userId;                 //当前用户id

    private String createByUserId;         //创建者id
    private String primaryPersonId;        //负责人Id

    private String projectName;            //项目名称

    private String projectGrade;           //项目级别
    private String projectProgress;        //项目进度
    private String projectStatus;          //项目状态
    private String projectType;            //项目类型

    private String industryDomain;         //行业领域

    private String officeId;               //归属部门
    private String areaId;                 //归属区域


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

    public String getIndustryDomain() {
        return industryDomain;
    }

    public void setIndustryDomain(String industryDomain) {
        this.industryDomain = industryDomain;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrimaryPersonId() {
        return primaryPersonId;
    }

    public void setPrimaryPersonId(String primaryPersonId) {
        this.primaryPersonId = primaryPersonId;
    }

    public String getCreateByUserId() {
        return createByUserId;
    }

    public void setCreateByUserId(String createByUserId) {
        this.createByUserId = createByUserId;
    }

    public String getProjectGrade() {
        return projectGrade;
    }

    public void setProjectGrade(String projectGrade) {
        this.projectGrade = projectGrade;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

}
