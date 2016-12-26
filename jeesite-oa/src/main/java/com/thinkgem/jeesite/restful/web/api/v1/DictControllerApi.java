package com.thinkgem.jeesite.restful.web.api.v1;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.restful.module.AppSearchUser;
import com.thinkgem.jeesite.restful.web.api.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/dicts", headers = "api-version=v1")
@Api(
        value = "/api/dicts",
        description = "数据字典相关API"
)
@Scope("prototype")
public class DictControllerApi extends BaseController {


    @Autowired
    private DictService dictService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private SystemService systemService;


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


    public final static Map<String, String> appProjectProgressMap = new HashMap();

    static {
        appProjectProgressMap.put("0", "预研10%");
        appProjectProgressMap.put("1", "立项20%");
        appProjectProgressMap.put("2", "尽调30%");
        appProjectProgressMap.put("3", "评审40%");
        appProjectProgressMap.put("4", "投决50%");
        appProjectProgressMap.put("5", "条款60");
        appProjectProgressMap.put("6", "基金70%");
        appProjectProgressMap.put("7", "协议80%");
        appProjectProgressMap.put("8", "工商90%");
        appProjectProgressMap.put("9", "投后100%");
    }

    @ApiOperation(value = "字典值获取", notes = "字典值获取（类型：项目级别projectGrade，项目进度projectProgress，项目状态projectStatus，项目类型projectType，行业领域industryDomain）")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> alldicts() {
        jsonResultModel = new JsonResultModel();
        try {
            List<String> dictlist = new LinkedList<>();
            dictlist.add("projectGrade");
            dictlist.add("projectProgress");
            dictlist.add("projectStatus");
            dictlist.add("projectType");
            dictlist.add("industryDomain");
            Map resultMap = new HashMap();
            Dict dict;
            List<Dict> list;
            for (String type : dictlist) {
                dict = new Dict();
                dict.setType(type);
                list = dictService.findList(dict);

                if (type.equals("projectProgress")) {
                    for (Dict itemDict : list) {
                        itemDict.setLabel(appProjectProgressMap.get(itemDict.getValue()));
                    }
                }

                resultMap.put(type, list);
            }
            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(resultMap);
            jsonResultModel.setMessage("success");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("参数校验失败：", e);
            jsonResultModel.setStateError();
            jsonResultModel.setMessage(e.getMessage());
        }
        return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
    }


    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    @RequestMapping(value = "/common/{userId}/offices", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> offices(@PathVariable String userId) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(userId, "userId不能为空");
            User checkUser = systemService.getUser(userId);
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
//            Office office = new Office();
//            office.setCurrentUser(checkUser);
            List<Office> list = officeService.findList(true);
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

    @ApiOperation(value = "获取区域", notes = "获取区域")
    @RequestMapping(value = "/common/{userId}/areas", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> areas(@PathVariable String userId) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(userId, "userId不能为空");
            User checkUser = systemService.getUser(userId);
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            Area area = new Area();
            area.setCurrentUser(checkUser);
            List<Area> list = areaService.findList(area);
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


    @ApiOperation(value = "获取人员列表", notes = "获取人员列表")
    @RequestMapping(value = "/common/{userId}/users", method = RequestMethod.GET)
    public ResponseEntity<JsonResultModel> users(@PathVariable String userId) {
        jsonResultModel = new JsonResultModel();
        try {
            Preconditions.checkNotNull(userId, "userId不能为空");
            User checkUser = systemService.getUser(userId);
            if (checkUser == null) {
                jsonResultModel.setMessage("用户不存在！");
                return new ResponseEntity<JsonResultModel>(jsonResultModel, HttpStatus.OK);
            }
            User userSearch = new User();
            userSearch.setCurrentUser(checkUser);
            List<User> userList = systemService.findUser(userSearch);
            List<AppSearchUser> appSearchUserList = new LinkedList<>();
            AppSearchUser appSearchUser;
            for (User user : userList) {
                appSearchUser = new AppSearchUser();
                appSearchUser.setId(user.getId());
                appSearchUser.setName(user.getName());
                appSearchUser.setLoginName(user.getLoginName());
                appSearchUser.setJianpin(user.getJianpin());
                appSearchUser.setQuanpin(user.getQuanpin());
                appSearchUserList.add(appSearchUser);
            }

            jsonResultModel.setStateSuccess();
            jsonResultModel.setData(appSearchUserList);
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
