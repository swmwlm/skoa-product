package com.thinkgem.jeesite.restful.web.api.v1;

import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.restful.modules.client.entity.HmacClient;
import com.thinkgem.jeesite.restful.modules.client.utils.HmacClientUtils;
import com.thinkgem.jeesite.restful.version.Version;
import com.thinkgem.jeesite.restful.version.VersionManager;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/api/version", headers = "api-version=V1.0.2")
@Api(
        value = "/api/version",
        description = "版本更新检查"
)
@Scope("prototype")
public class VersionControllerApi extends BaseController {

    @ApiOperation(value = "版本更新检查", notes = "版本更新检查")
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> check(HttpServletRequest request) {
        jsonResultModel = new JsonResultModel();
        try {
            String appId=request.getHeader("appId");
            String version=request.getHeader("api-version");
            HmacClient hmacClient= HmacClientUtils.getByAppId(appId);
            String clientType=hmacClient.getClientType();

            //检查客户端类型值是否有效
            if (!Version.checkType(clientType)) {
                jsonResultModel.setStateError();
                jsonResultModel.setMessage("无效客户端类型！");
                return new ResponseEntity<>(jsonResultModel, HttpStatus.OK);
            }
            Version resultVersion = VersionManager.getInstance().check(version, clientType);

            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(resultVersion);
            jsonResultModel.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("参数校验失败：", e);
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<>(jsonResultModel, HttpStatus.OK);
    }

}
