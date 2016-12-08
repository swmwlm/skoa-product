package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.restful.module.AppUser;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(value = "/api/projectInfos", headers = "api-version=v1")
@Api(
        value = "/api/projectInfos",
        description = "项目相关API"
)
public class ProjectInfoControllerApi extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation(value = "获取项目列表", notes = "获取项目列表")
    @RequestMapping(value = "/{userId}", method = RequestMethod.POST)
    public ResponseEntity<JsonResultModel> projectInfos(@PathVariable String userId, @RequestBody ProjectInfo projectInfo, HttpServletRequest request, HttpServletResponse response) {
        jsonResultModel = new JsonResultModel();
        try {
            //todo 待完善查询和返回数据。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。

            Preconditions.checkNotNull(userId, "userId不能为空");
            User checkUser = systemService.getUser(userId);
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            Page<ProjectInfo> projectInfoPage = projectInfoService.findPageDSFforAPP(new Page<ProjectInfo>(request, response), projectInfo, checkUser.getId());
            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(projectInfoPage);
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
