package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.restful.version.Version;
import com.thinkgem.jeesite.restful.version.VersionManager;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/version", headers = "api-version=v1")
@Api(
        value = "/api/users",
        description = "用户相关API"
)
@Scope("prototype")
public class VersionControllerApi extends BaseController {

    @ApiOperation(value = "版本更新检查", notes = "版本更新检查")
    @RequestMapping(value = "/check/{client}/{version}", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> check(@PathVariable String client,@PathVariable String version) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(client, "客户端类型client不能为空");
            Preconditions.checkNotNull(version, "客户端类型version不能为空");

            //检查客户端类型值是否有效
            if (!Version.checkType(client)) {
                jsonResultModel.setStateError();
                jsonResultModel.setMessage("无效客户端类型！");
                return new ResponseEntity<>(jsonResultModel, HttpStatus.OK);
            }
            Version resultVersion = VersionManager.me().check(version, client);

            if (null==resultVersion) {
                jsonResultModel.setStateError();
                jsonResultModel.setMessage("客户端无更新");
                return new ResponseEntity<>(jsonResultModel, HttpStatus.OK);
            }

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
