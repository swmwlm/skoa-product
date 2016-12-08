package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/dicts", headers = "api-version=v1")
@Api(
        value = "/api/dicts",
        description = "数据字典相关API"
)
public class DictControllerApi extends BaseController {


    @Autowired
    private DictService dictService;

    @ApiOperation(value = "字典值获取", notes = "字典值获取（类型：项目级别projectGrade，项目进度projectProgress，项目状态projectStatus，项目类型projectType，行业领域industryDomain）")
    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> dicts(@PathVariable String type) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(type, "type不能为空");
            //type刷选
            if (!type.equals("projectGrade")
                    && !type.equals("projectProgress")
                    && !type.equals("projectStatus")
                    && !type.equals("projectType")
                    && !type.equals("industryDomain")) {
                jsonResultModel.setStateError();
                jsonResultModel.setMessage("无效类型！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            Dict dict = new Dict();
            dict.setType(type);
            List<Dict> list = dictService.findList(dict);
            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(list);
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
