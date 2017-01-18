package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.restful.module.AppProjectInfo;
import com.thinkgem.jeesite.restful.module.AppProjectInfoDetail;
import com.thinkgem.jeesite.restful.module.AppProjectInfoSearch;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/projectInfos", headers = "api-version=v1")
@Api(
        value = "/api/projectInfos",
        description = "项目相关API"
)
@Scope("prototype")
public class ProjectInfoControllerApi extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation(value = "获取项目列表", notes = "获取项目列表")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<JsonResultModel> projectInfos(@RequestBody AppProjectInfoSearch appProjectInfoSearch, HttpServletRequest request, HttpServletResponse response) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(appProjectInfoSearch.getUserId(), "userId不能为空");

            User checkUser = systemService.getUser(appProjectInfoSearch.getUserId());
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }

            ProjectInfo projectInfo = new ProjectInfo();

            projectInfo.setCurrentUser(checkUser);

//            创建人
            if (StringUtils.isNotBlank(appProjectInfoSearch.getCreateByUserId())) {
                User createBy = new User();
                createBy.setId(appProjectInfoSearch.getCreateByUserId());
                projectInfo.setCreateBy(createBy);
            }
//            负责人（主负责人，副负责人均包括）
            if (StringUtils.isNotBlank(appProjectInfoSearch.getPrimaryPersonId())) {
                User primaryPerson = new User();
                primaryPerson.setId(appProjectInfoSearch.getPrimaryPersonId());
                projectInfo.setPrimaryPerson(primaryPerson);
            }

//            项目名称
            if (StringUtils.isNotBlank(appProjectInfoSearch.getProjectName())) {
                projectInfo.setProjectName(appProjectInfoSearch.getProjectName());
            }

//            项目级别
            if (StringUtils.isNotBlank(appProjectInfoSearch.getProjectGrade())) {
                projectInfo.setProjectGrade(appProjectInfoSearch.getProjectGrade());
            }
//            项目进度
            if (StringUtils.isNotBlank(appProjectInfoSearch.getProjectProgress())) {
                projectInfo.setProjectProgress(appProjectInfoSearch.getProjectProgress());
            }
//            项目状态
            if (StringUtils.isNotBlank(appProjectInfoSearch.getProjectStatus())) {
                projectInfo.setProjectStatus(appProjectInfoSearch.getProjectStatus());
            }
//            项目阶段
            if (StringUtils.isNotBlank(appProjectInfoSearch.getProjectType())) {
                projectInfo.setProjectType(appProjectInfoSearch.getProjectType());
            }

//            行业领域
            if (StringUtils.isNotBlank(appProjectInfoSearch.getIndustryDomain())) {
                projectInfo.setIndustryDomain(appProjectInfoSearch.getIndustryDomain());
            }

//            归属部门
            if (StringUtils.isNotBlank(appProjectInfoSearch.getOfficeId())) {
                Office office = new Office();
                office.setId(appProjectInfoSearch.getOfficeId());
                projectInfo.setOffice(office);
            }
//            归属区域
            if (StringUtils.isNotBlank(appProjectInfoSearch.getAreaId())) {
                Area area = new Area();
                area.setId(appProjectInfoSearch.getAreaId());
                projectInfo.setArea(area);
            }

            Page<ProjectInfo> page = projectInfoService.findPageDSFforAPP(new Page<ProjectInfo>(request, response), projectInfo, checkUser.getId());

            //返回数据处理一下,换成app需要的数据
            Page<AppProjectInfo> appProjectInfoPage = new Page<AppProjectInfo>();
            appProjectInfoPage.setPageNo(page.getPageNo());
            appProjectInfoPage.setPageSize(page.getPageSize());
            appProjectInfoPage.setCount(page.getCount());

            List<AppProjectInfo> appProjectInfoList = new LinkedList<>();
            if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
                List<ProjectInfo> projectInfoList = page.getList();
                AppProjectInfo appProjectInfo;
                for (ProjectInfo item : projectInfoList) {
                    appProjectInfo = new AppProjectInfo();
                    appProjectInfo.setId(item.getId());
                    appProjectInfo.setProjectName(item.getProjectName());
                    appProjectInfo.setProjectProgress(item.getProjectProgress());
                    appProjectInfo.setProjectProgressName(DictUtils.getDictLabel(item.getProjectProgress(), "projectProgress", "暂无进度"));
                    appProjectInfo.setCreateByUserName(UserUtils.get(item.getCreateBy().getId()).getName());
                    appProjectInfo.setPrimaryPersonName(UserUtils.get(item.getPrimaryPerson().getId()).getName());
                    appProjectInfo.setTeamMemberNames(item.getTeamMemberNames());
                    appProjectInfo.setIndustryDomainName(DictUtils.getDictLabel(item.getIndustryDomain(), "industryDomain", ""));
                    appProjectInfo.setUpdateDate(item.getUpdateDate());
                    appProjectInfo.setUpdateByName(UserUtils.get(item.getUpdateBy().getId()).getName());
                    appProjectInfoList.add(appProjectInfo);
                }
            }
            appProjectInfoPage.setList(appProjectInfoList);

            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(appProjectInfoPage);
            jsonResultModel.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("参数校验失败：", e);
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);


    }


    @ApiOperation(value = "获取项目详情", notes = "获取项目详情，url包含用户id和项目id")
    @RequestMapping(value = "/{userId}/{projectInfoId}", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> projectInfo(@PathVariable String userId, @PathVariable String projectInfoId) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(userId, "userId不能为空");
            Preconditions.checkNotNull(projectInfoId, "projectInfoId不能为空");
            User checkUser = systemService.getUser(userId);
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            ProjectInfo projectInfo = projectInfoService.get(projectInfoId);

            AppProjectInfoDetail appProjectInfoDetail = new AppProjectInfoDetail();
            appProjectInfoDetail.setId(projectInfo.getId());
            appProjectInfoDetail.setProjectName(projectInfo.getProjectName());
            appProjectInfoDetail.setProjectGrade(projectInfo.getProjectGrade());
            appProjectInfoDetail.setContent(projectInfo.getContent());
//            appProjectInfoDetail.setPrimaryPersonName(UserUtils.get(projectInfo.getPrimaryPerson().getId()).getName());
            appProjectInfoDetail.setPrimaryPersonName(projectInfo.getPrimaryPerson().getName());
            appProjectInfoDetail.setAreaName(projectInfo.getArea().getName());
            appProjectInfoDetail.setIndustryDomain(DictUtils.getDictLabel(projectInfo.getIndustryDomain(), "industryDomain", ""));
            appProjectInfoDetail.setMainBusiness(projectInfo.getMainBusiness());
            appProjectInfoDetail.setRecommendedMan(projectInfo.getRecommendedMan());
            appProjectInfoDetail.setRecommendedDate(projectInfo.getRecommendedDate());
            appProjectInfoDetail.setAnnualIncome(projectInfo.getAnnualIncome());
            appProjectInfoDetail.setAnnualNetProfit(projectInfo.getAnnualNetProfit());
            appProjectInfoDetail.setProjectType(DictUtils.getDictLabel(projectInfo.getProjectType(), "projectType", ""));
            appProjectInfoDetail.setStartDate(projectInfo.getStartDate());
            appProjectInfoDetail.setIntendedMoney(projectInfo.getIntendedMoney());
            appProjectInfoDetail.setProjectProgress(projectInfo.getProjectProgress());
            appProjectInfoDetail.setProjectProgressName(DictUtils.getDictLabel(projectInfo.getProjectProgress(), "projectProgress", ""));


//            AppProjectInfoDetail appProjectInfoDetail = (AppProjectInfoDetail) new SimpleBeanCopier(ProjectInfo.class, AppProjectInfoDetail.class).copy(projectInfo);
//            appProjectInfoDetail.setPrimaryPersonName(UserUtils.get(projectInfo.getPrimaryPerson().getId()).getName());
//            appProjectInfoDetail.setIndustryDomain(DictUtils.getDictLabel(projectInfo.getIndustryDomain(), "industryDomain", ""));
//            appProjectInfoDetail.setProjectType(DictUtils.getDictLabel(projectInfo.getProjectType(), "projectType", ""));
//            appProjectInfoDetail.setProjectProgressName(DictUtils.getDictLabel(projectInfo.getProjectProgress(), "projectProgress", ""));

            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(appProjectInfoDetail);
            jsonResultModel.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("参数校验失败：", e);
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
    }


}
