package com.thinkgem.jeesite.restful.module;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AppUser {
    private String id;      //用户id
    private String photo;   //头像
    private String loginName;//登录名
    private String password;//密码
    private String confirmPassword;//确认密码
    private String name;    //姓名
    private String mobile;  //手机
    private String email;   //邮箱
    private Long faqiNum;//发起的项目数目
    private Long fuzeNum;//负责的项目数目
    private String department;//部门名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getFaqiNum() {
        return faqiNum;
    }

    public void setFaqiNum(Long faqiNum) {
        this.faqiNum = faqiNum;
    }

    public Long getFuzeNum() {
        return fuzeNum;
    }

    public void setFuzeNum(Long fuzeNum) {
        this.fuzeNum = fuzeNum;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
