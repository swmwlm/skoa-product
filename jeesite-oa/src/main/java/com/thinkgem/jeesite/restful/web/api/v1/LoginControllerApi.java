package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.JwtTokenUtils;
import com.thinkgem.jeesite.restful.module.AppUser;
import com.thinkgem.jeesite.restful.module.RememberedTime;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping(value = "/api/login", headers = "api-version=v1")
@Api(
        value = "/api/login",
        description = "登录相关API"
)
@Scope("prototype")
public class LoginControllerApi extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private ProjectInfoService projectInfoService;

    //------------------- 登录 -----------------------------------------------------------------
    @ApiOperation(value = "登录", notes = "登录")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<JsonResultModel> login(@ApiParam(
            value = "appUser object",
            required = true
    ) @RequestBody AppUser appUser, HttpServletRequest request, HttpServletResponse response) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(appUser.getLoginName(), "登录名不能为空");
            Preconditions.checkNotNull(appUser.getPassword(), "密码不能为空");
            User checkUser = systemService.getUserByLoginName(appUser.getLoginName());
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            if (Global.NO.equals(checkUser.getLoginFlag())) {
                jsonResultModel.setMessage("当前用户被禁止登陆！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            if (!systemService.validatePassword(appUser.getPassword(), checkUser.getPassword())) {
                jsonResultModel.setMessage("用户名或者密码错误！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }

            AppUser resultappUser = new AppUser();

            resultappUser.setId(checkUser.getId());
            resultappUser.setDepartment(checkUser.getOffice() != null ? StringUtils.defaultIfBlank(checkUser.getOffice().getName(), "") : "");
            resultappUser.setEmail(StringUtils.defaultIfBlank(checkUser.getEmail(), ""));
            resultappUser.setMobile(StringUtils.defaultIfBlank(checkUser.getMobile(), ""));
            resultappUser.setPhoto(StringUtils.defaultIfBlank(checkUser.getPhoto(), ""));
            resultappUser.setName(StringUtils.defaultIfBlank(checkUser.getName(), ""));
            resultappUser.setPassword("******");
            resultappUser.setLoginName(StringUtils.defaultIfBlank(checkUser.getLoginName(), ""));


            //查询我发起的项目数目
            ProjectInfo projectInfo1 = new ProjectInfo();
            projectInfo1.setCreateBy(checkUser);
            Page<ProjectInfo> page1 = projectInfoService.findPageDSFforAPP(new Page<ProjectInfo>(request, response), projectInfo1, checkUser.getId());
            resultappUser.setFaqiNum(page1.getCount());

            //查询我负责的项目数目
            ProjectInfo projectInfo2 = new ProjectInfo();
            projectInfo2.setPrimaryPerson(checkUser);
            Page<ProjectInfo> page2 = projectInfoService.findPageDSFforAPP(new Page<ProjectInfo>(request, response), projectInfo2, checkUser.getId());
            resultappUser.setFuzeNum(page2.getCount());

            //生成TOKEN，并返回给用户。
            String token = JwtTokenUtils.createToken(checkUser.getId());
            if (StringUtils.isBlank(token)) {
                jsonResultModel.setMessage("生成TOKEN失败！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            resultappUser.setToken(token);

            jsonResultModel.setStateSuccess();
            jsonResultModel.setMessage("success");
            jsonResultModel.setData(resultappUser);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("参数校验失败：", e);
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
    }


    //------------------- 记住密码 -----------------------------------------------------------------
    @ApiOperation(value = "记住密码", notes = "获取记住密码天数和服务器当前时间")
    @RequestMapping(value = "/beRememberedTime", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> beRememberedTime() {
        jsonResultModel = new JsonResultModel();
        try {
            RememberedTime rememberedTime = new RememberedTime();
            rememberedTime.setDays(30);
            rememberedTime.setNowTime(DateUtils.getCurrentDateTime());
            jsonResultModel.setStateSuccess();
            jsonResultModel.setMessage("success");
            jsonResultModel.setData(rememberedTime);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("参数校验失败：", e);
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
    }


    //------------------- 注册 -----------------------------------------------------------------
    @ApiOperation(value = "注册", notes = "注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<JsonResultModel> register(@ApiParam(
            value = "appUser object",
            required = true
    ) @RequestBody AppUser appUser) {
        jsonResultModel = new JsonResultModel();
        try {
            if (!Global.getConfig("app.allowRegister").equals("true")) {
                jsonResultModel.setMessage("注册已关闭，请联系管理员！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }

            Preconditions.checkNotNull(appUser.getLoginName(), "登录名不能为空");
            Preconditions.checkNotNull(appUser.getPassword(), "密码不能为空");
            Preconditions.checkNotNull(appUser.getConfirmPassword(), "确认密码不能为空");
            User checkUser = systemService.getUserByLoginName(appUser.getLoginName());
            if (checkUser != null) {
                jsonResultModel.setMessage("登录名已存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            if (!appUser.getPassword().equals(appUser.getConfirmPassword())) {
                jsonResultModel.setMessage("密码不一致！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }

            User user = new User();
            user.setLoginName(appUser.getLoginName());
            user.setName(appUser.getLoginName());
            user.setPassword(systemService.entryptPassword(appUser.getPassword()));
            user.setEmail(appUser.getEmail());
            user.setPhone(appUser.getPhoto());
            systemService.saveAppUser(user);


            jsonResultModel.setStateSuccess();
            jsonResultModel.setMessage("注册成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("参数校验失败：", e);
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
    }

}
