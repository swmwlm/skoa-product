package com.thinkgem.jeesite.restful.module;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class RememberedTime {

    private Integer days;// 天数
    private Date nowTime;// 当前时间

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
