package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.oa.service.OaNotifyService;
import com.thinkgem.jeesite.modules.project.entity.ProjectInfo;
import com.thinkgem.jeesite.modules.project.service.ProjectInfoService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.restful.module.AppOaNotify;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.impl.util.CollectionUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/oaNotifys", headers = "api-version=v1")
@Api(
        value = "/api/oaNotifys",
        description = "通知相关API"
)
public class OaNotifyControllerApi extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private OaNotifyService oaNotifyService;

    @ApiOperation(value = "获取通知列表", notes = "获取项目列表,readFlag: 0:未读,1:已读")
    @RequestMapping(value = "/{userId}/{readFlag}", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> projectInfos(@PathVariable String userId, @PathVariable String readFlag, HttpServletRequest request, HttpServletResponse response) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(userId, "userId不能为空");
            Preconditions.checkNotNull(readFlag, "readType不能为空");
            User checkUser = systemService.getUser(userId);
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            OaNotify oaNotify = new OaNotify();
            oaNotify.setReadFlag(readFlag);
            oaNotify.setSelf(true);
            oaNotify.setReadFlag(readFlag);
            oaNotify.setCurrentUser(checkUser);
            Page<OaNotify> page = oaNotifyService.find(new Page<OaNotify>(request, response), oaNotify);

            //返回数据处理一下,换成app需要的数据
            Page<AppOaNotify> appOaNotifyPage = new Page<AppOaNotify>();
            appOaNotifyPage.setPageNo(page.getPageNo());
            appOaNotifyPage.setPageSize(page.getPageSize());
            appOaNotifyPage.setCount(page.getCount());

            List<AppOaNotify> appOaNotifyList = new LinkedList<>();
            if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
                List<OaNotify> oaNotifyList = page.getList();
                AppOaNotify appOaNotify;
                for (OaNotify notify : oaNotifyList) {
                    appOaNotify = new AppOaNotify();
                    appOaNotify.setType(notify.getType());
                    appOaNotify.setTitle(notify.getTitle());
                    appOaNotify.setContent(notify.getContent());
                    appOaNotify.setReadFlag(notify.getReadFlag());
                    appOaNotify.setCreateByName(notify.getCreateBy().getName());
                    appOaNotify.setCreateByPhoto(notify.getCreateBy().getPhoto());
                    appOaNotify.setCreateDate(notify.getCreateDate());
                    appOaNotify.setProjectInfoId(StringUtils.isNotBlank(notify.getRemarks()) ? notify.getRemarks() : "");
                    appOaNotifyList.add(appOaNotify);
                }
            }
            appOaNotifyPage.setList(appOaNotifyList);

            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(appOaNotifyPage);
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