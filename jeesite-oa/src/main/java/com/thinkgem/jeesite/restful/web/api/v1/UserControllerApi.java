package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.TokenUtils;
import com.thinkgem.jeesite.restful.module.AppUser;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(value = "/api/users", headers = "api-version=v1")
@Api(
        value = "/api/users",
        description = "用户相关API"
)
@Scope("prototype")
public class UserControllerApi extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private ProjectInfoService projectInfoService;

    @ApiOperation(value = "获取用户基本信息", notes = "获取用户基本信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> userinfo(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(id, "id不能为空");

            System.out.println("测试：id:" + getJwtUserId());

            User checkUser = systemService.getUser(id);
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            if (Global.NO.equals(checkUser.getLoginFlag())) {
                jsonResultModel.setMessage("当前用户被禁止登陆！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }

            AppUser appUser = new AppUser();

            appUser.setId(checkUser.getId());
            appUser.setDepartment(checkUser.getOffice() != null ? StringUtils.defaultIfBlank(checkUser.getOffice().getName(), "") : "");
            appUser.setEmail(StringUtils.defaultIfBlank(checkUser.getEmail(), ""));
            appUser.setMobile(StringUtils.defaultIfBlank(checkUser.getMobile(), ""));
            appUser.setPhoto(StringUtils.defaultIfBlank(checkUser.getPhoto(), ""));
            appUser.setName(StringUtils.defaultIfBlank(checkUser.getName(), ""));
            appUser.setPassword("******");
            appUser.setLoginName(StringUtils.defaultIfBlank(checkUser.getLoginName(), ""));

            //查询我发起的项目数目
            ProjectInfo projectInfo1 = new ProjectInfo();
            projectInfo1.setCreateBy(checkUser);
            Page<ProjectInfo> page1 = projectInfoService.findPageDSFforAPP(new Page<ProjectInfo>(request, response), projectInfo1, checkUser.getId());
            appUser.setFaqiNum(page1.getCount());

            //查询我负责的项目数目
            ProjectInfo projectInfo2 = new ProjectInfo();
            projectInfo2.setPrimaryPerson(checkUser);
            Page<ProjectInfo> page2 = projectInfoService.findPageDSFforAPP(new Page<ProjectInfo>(request, response), projectInfo2, checkUser.getId());
            appUser.setFuzeNum(page2.getCount());

            appUser.setToken(request.getHeader("token"));

            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(appUser);
            jsonResultModel.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("参数校验失败：", e);
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);


    }


    @ApiOperation(value = "用户退出", notes = "用户退出")
    @RequestMapping(value = "/quit", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> quit(HttpServletRequest request, HttpServletResponse response) {
        jsonResultModel = new JsonResultModel();
        try {
            TokenUtils.removeToken(getJwtUserId(), request.getHeader("token"));
            jsonResultModel.setStateSuccess();
            jsonResultModel.setData("退出成功！");
            jsonResultModel.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
    }


}
