package com.thinkgem.jeesite.restful.module;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.Date;

//需要序列化null的话，在model上加下面注解
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AppProjectInfoDetail {

    private String id;  //项目id
    private String projectName;                  // 项目名称
    private String projectGrade;                // 项目级别
    private String content;                    // 项目介绍
    private String primaryPersonName;         // 项目负责人
    private String areaName;                 // 归属区域
    private String industryDomain;          // 行业领域
    private String mainBusiness;           // 主营业务
    private String recommendedMan;        // 项目推荐人
    private Date   recommendedDate;        // 项目的推荐时间
    private String annualIncome;        // 年收入
    private String annualNetProfit;    // 年净利润
    private String projectType;       // 项目类型
    private Date   startDate;          // 项目的开始时间
    private BigDecimal intendedMoney;        // 拟投金额
    private String projectProgress;         // 项目进度
    private String projectProgressName;    // 项目进度名称

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

    public String getProjectGrade() {
        return projectGrade;
    }

    public void setProjectGrade(String projectGrade) {
        this.projectGrade = projectGrade;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrimaryPersonName() {
        return primaryPersonName;
    }

    public void setPrimaryPersonName(String primaryPersonName) {
        this.primaryPersonName = primaryPersonName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getIndustryDomain() {
        return industryDomain;
    }

    public void setIndustryDomain(String industryDomain) {
        this.industryDomain = industryDomain;
    }

    public String getMainBusiness() {
        return mainBusiness;
    }

    public void setMainBusiness(String mainBusiness) {
        this.mainBusiness = mainBusiness;
    }

    public String getRecommendedMan() {
        return recommendedMan;
    }

    public void setRecommendedMan(String recommendedMan) {
        this.recommendedMan = recommendedMan;
    }

    public Date getRecommendedDate() {
        return recommendedDate;
    }

    public void setRecommendedDate(Date recommendedDate) {
        this.recommendedDate = recommendedDate;
    }

    public String getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(String annualIncome) {
        this.annualIncome = annualIncome;
    }

    public String getAnnualNetProfit() {
        return annualNetProfit;
    }

    public void setAnnualNetProfit(String annualNetProfit) {
        this.annualNetProfit = annualNetProfit;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getIntendedMoney() {
        return intendedMoney;
    }

    public void setIntendedMoney(BigDecimal intendedMoney) {
        this.intendedMoney = intendedMoney;
    }

    public String getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(String projectProgress) {
        this.projectProgress = projectProgress;
    }

    public String getProjectProgressName() {
        return projectProgressName;
    }

    public void setProjectProgressName(String projectProgressName) {
        this.projectProgressName = projectProgressName;
    }
}
