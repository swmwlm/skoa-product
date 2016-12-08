package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.restful.module.AppUser;
import com.thinkgem.jeesite.restful.module.RememberedTime;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/login", headers = "api-version=v1")
@Api(
        value = "/api/login",
        description = "登录相关API"
)
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
            if (!systemService.validatePassword(appUser.getPassword(), checkUser.getPassword())) {
                jsonResultModel.setMessage("用户名或者密码错误！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            jsonResultModel.setStateSuccess();
            jsonResultModel.setMessage("success");
            Map resultMap = new HashMap();
            resultMap.put("id", checkUser.getId());
            jsonResultModel.setData(resultMap);
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
            rememberedTime.setDays(1);
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


}
