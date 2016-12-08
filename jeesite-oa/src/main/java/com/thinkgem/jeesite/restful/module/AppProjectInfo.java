package com.thinkgem.jeesite.restful.module;


import com.google.common.collect.Lists;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfoProgress;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

//todo 待完善

public class AppProjectInfo {

    private String id;

    private Office office;		// 归属部门
    private Area area;		// 归属区域
    private String projectName;		// 项目名称
    private String projectGrade;		// 项目级别
    private User primaryPerson;		// 项目负责人
    private String teamMembers;		// 项目小组成员
    private String teamMemberNames;		// 项目小组成员
    private String industryDomain;		// 行业领域
    private String mainBusiness;		// 主营业务
    private String content;		// 项目介绍
    private String filepath;		// 附件路径
    private String annualIncome;		// 年收入
    private String annualNetProfit;		// 年净利润
    private String projectProgress;		// 项目进度
    private Date startDate;		// 项目的开始时间
    private String recommendedMan;		// 项目推荐人
    private Date recommendedDate;		// 项目的推荐时间
    private String projectType;		// 项目类型
    private BigDecimal intendedMoney;		// 拟投金额
    private String projectStatus;		// 项目状态
    private List<ProjectInfoProgress> projectInfoProgressList = Lists.newArrayList();		// 子表列表

    private String remarks;	// 备注
    private User createBy;	// 创建者
    private Date createDate;	// 创建日期
    private User updateBy;	// 更新者
    private Date updateDate;	// 更新日期
    private String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）


}
