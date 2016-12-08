package com.thinkgem.jeesite.restful.module;


import java.util.Date;

public class RememberedTime {

    private Integer days;// 登录名
    private Date nowTime;// 密码

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Date getNowTime() {
        return nowTime;
    }

    public void setNowTime(Date nowTime) {
        this.nowTime = nowTime;
    }

}
