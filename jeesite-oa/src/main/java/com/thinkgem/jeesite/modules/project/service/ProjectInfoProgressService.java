/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.project.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.oa.service.OaNotifyService;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoDao;
import com.thinkgem.jeesite.modules.project.dao.ProjectInfoProgressDao;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfoProgress;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 项目进度管理Service
 *
 * @author evan
 * @version 2016-10-25
 */
@Service
@Transactional(readOnly = true)
public class ProjectInfoProgressService extends CrudService<ProjectInfoProgressDao, ProjectInfoProgress> {

    @Autowired
    private ProjectInfoProgressDao projectInfoProgressDao;

    @Autowired
    private ProjectInfoDao projectInfoDao;

    @Autowired
    private OaNotifyService oaNotifyService;


    @Transactional(readOnly = false)
    public void addProjectInfoProgress(ProjectInfo projectInfo, ProjectInfoProgress projectInfoProgress) {

        projectInfoDao.update(projectInfo);

        projectInfoProgress.setProjectInfo(projectInfo);
        projectInfoProgress.preInsert();
        projectInfoProgressDao.insert(projectInfoProgress);

        //根据项目获取需要通知的人员
        String oaNotifyRecordIds = UserUtils.getNotifyUserIdsString(projectInfo, null, UserUtils.getUser().getId());
        StringBuilder content = new StringBuilder();
        content.append("项目进度由");
        content.append("“");
        content.append(DictUtils.getDictLabels(projectInfoProgress.getStatusOrigin(), "projectProgress", "暂无进度"));
        content.append("”");
        content.append("更新为");
        content.append("“");
        content.append(DictUtils.getDictLabels(projectInfoProgress.getStatusCurrent(), "projectProgress", "暂无进度"));
        content.append("”");
        content.append("。");
        content.append("变更备注:"+projectInfoProgress.getRemarks());

        System.out.println("【项目进度更新：oaNotifyRecordIds:" + oaNotifyRecordIds + "。" + content.toString() + "】");

        //添加到我的通告
        OaNotify oaNotify = new OaNotify();
        oaNotify.setType("5");
        oaNotify.setTitle(projectInfo.getProjectName());
        oaNotify.setContent(content.toString());
        oaNotify.setStatus("1");
        oaNotify.setOaNotifyRecordIds(oaNotifyRecordIds);
        oaNotify.setRemarks(projectInfo.getId());
        oaNotifyService.save(oaNotify);

    }

}