package com.thinkgem.jeesite.modules.tasks;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.batchlog.entity.SysBatchlog;
import com.thinkgem.jeesite.modules.batchlog.service.SysBatchlogService;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.oa.service.OaNotifyService;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Lazy(value=false)
public class MyQuartzJob {
	@Autowired
	private ProjectInfoService projectInfoService;
	@Autowired
	private OaNotifyService oaNotifyService;
	@Autowired
	private SysBatchlogService sysBatchlogService;

	/**
	 * 每天早上0:30分执行一次;
	 * 通知项目更新时间超过15天未做更新的负责人,项目状态为负责人管理(1) 阶段;
	 */
	@Scheduled(cron = "0 30 0 * * ?")
	//@Scheduled(cron = "0 * * * * ?")//调试方便,设置每分钟执行一次;
	public void sendOaNotify4ProjectInfoUpdateGte15days(){
		String jobName="JOB_SendOaNotify4ProjectInfoUpdateGte15days";
		Date lastExecuteTime=sysBatchlogService.selectLastExecuteTimeByJobName(jobName);

		Calendar calendar = Calendar.getInstance();
		Date currentExecuteTime = DateUtils.createDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE) - 1, 0);

		if (lastExecuteTime != null && lastExecuteTime.after(currentExecuteTime)) {
			return;
		}
		if (lastExecuteTime != null && lastExecuteTime.before(currentExecuteTime)) {
			List<ProjectInfo> projectInfoList=projectInfoService.selectProjectInfoUpdateGte15days(lastExecuteTime,currentExecuteTime);

			if(!Collections3.isEmpty(projectInfoList)){
				OaNotify oaNotify = new OaNotify();
				oaNotify.setType("4");//类型:项目动态
				oaNotify.setStatus("1");//发布状态
				oaNotify.setCreateBy(new User("1"));
				oaNotify.setUpdateBy(new User("1"));
				for(ProjectInfo p :projectInfoList){
					oaNotify.setTitle(p.getProjectName()+" 已经超过15天没有更新啦");
					oaNotify.setContent(p.getProjectName()+" 最后一次更新于"+ DateUtils.formatDateTime(p.getUpdateDate()));
					//1.Joiner先以','拼装主负责人和副负责人 2.Splitter以','拆分成一个可迭代的集合 3.把集合转化为set,去除重复的人员id
					Set<String> idSet=new HashSet<>(Splitter.on(',').omitEmptyStrings().trimResults().splitToList(Joiner.on(',').join(p.getPrimaryPerson(),p.getTeamMembers())));

					oaNotify.setOaNotifyRecordIds(Joiner.on(',').join(idSet));
					oaNotify.setRemarks(p.getId());
					//添加到我的通告
					oaNotifyService.save(oaNotify);
				}
			}
		}

		//添加一条任务日志
		SysBatchlog sysBatchlog = new SysBatchlog();
		sysBatchlog.setId("");
		sysBatchlog.setJobname(jobName);
		//跑批任务 设置为系统管理员账户
		sysBatchlog.setCreateBy(new User("1"));
		sysBatchlog.setUpdateBy(new User("1"));
		sysBatchlog.setLastexecutetime(currentExecuteTime);
		sysBatchlogService.save(sysBatchlog);
	}


	/*@Scheduled(cron = "*//*5 * * * * ?")//每隔5秒执行一次
	public void test() throws Exception {
		System.out.println("Test is working......");
	}*/
	// @Scheduled(fixedDelay=1000)  //第一种方式
	// fixedDelay延时多少毫秒，多少毫秒执行一次
    /*
        1 Seconds (0-59)
        2 Minutes (0-59)
        3 Hours (0-23)
        4 Day of month (1-31)
        5 Month (1-12 or JAN-DEC)
        6 Day of week (1-7 or SUN-SAT)
        7 Year (1970-2099)
        取值：可以是单个值，如6；
            也可以是个范围，如9-12；
            也可以是个列表，如9,11,13
            也可以是任意取值，使用*
    */
	// 0 * * * * * 代表每分钟执行一次
    /*
         2011-09-07 09:23:00
        2011-09-07 09:24:00
        2011-09-07 09:25:00
        2011-09-07 09:26:00
     */
	/*@Scheduled(cron="0/1 * * * * ?")
	public void execute() {
		long ms = System.currentTimeMillis();
		System.out.println(ms);
	}*/
}
