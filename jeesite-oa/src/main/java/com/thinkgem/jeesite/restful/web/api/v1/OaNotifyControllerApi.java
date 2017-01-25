package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.oa.service.OaNotifyService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.restful.module.AppOaNotify;
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
@RequestMapping(value = "/api/oaNotifys", headers = "api-version=V1.0.2")
@Api(
        value = "/api/oaNotifys",
        description = "通知相关API"
)
@Scope("prototype")
public class OaNotifyControllerApi extends BaseController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private OaNotifyService oaNotifyService;

    @ApiOperation(value = "获取通知列表", notes = "获取通知列表,readFlag: 0:未读,1:已读")
    @RequestMapping(value = "/{readFlag}", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> projectInfos(@PathVariable String readFlag, HttpServletRequest request, HttpServletResponse response) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(readFlag, "readFlag不能为空");
            User checkUser = systemService.getUser(getJwtUserId());
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            OaNotify oaNotify = new OaNotify();
            oaNotify.setReadFlag(readFlag);
            oaNotify.setSelf(true);
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
                    appOaNotify.setId(notify.getId());
                    appOaNotify.setType(notify.getType());
                    appOaNotify.setTitle(notify.getTitle());
                    appOaNotify.setContent(StringUtils.defaultIfBlank(notify.getContent(), ""));
                    appOaNotify.setReadFlag(StringUtils.defaultIfBlank(notify.getReadFlag(), ""));
                    appOaNotify.setCreateByName(StringUtils.defaultIfBlank(UserUtils.get(notify.getCreateBy().getId()).getName(), ""));
                    appOaNotify.setCreateByPhoto(StringUtils.defaultIfBlank(UserUtils.get(notify.getCreateBy().getId()).getPhoto(), ""));
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


    @ApiOperation(value = "设置已读", notes = "设置已读，更新阅读状态")
    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> setRead(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(id, "id不能为空");
            User checkUser = systemService.getUser(getJwtUserId());
            Preconditions.checkNotNull(checkUser, "用户不存在");
            OaNotify oaNotify = oaNotifyService.get(id);
            Preconditions.checkNotNull(oaNotify, "消息不存在");
            oaNotifyService.updateReadFlag(oaNotify, checkUser);
            jsonResultModel.setStateSuccess();
            jsonResultModel.setData("更新成功！");
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
