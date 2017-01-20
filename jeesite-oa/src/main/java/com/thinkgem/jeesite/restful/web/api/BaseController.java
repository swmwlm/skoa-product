/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.restful.web.api;

import cn.jflow.common.context.ServletRequestAttributes;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.json.ResponseJsonUtil;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseController {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected JsonResultModel jsonResultModel = new JsonResultModel();

    /**
     * 客户端返回JSON字符串
     *
     * @param response
     * @param object
     * @return
     */
    protected String renderString(HttpServletResponse response, Object object) {
        return ResponseJsonUtil.renderString(response, JsonMapper.toJsonString(object), "application/json");
    }

    /**
     * 客户端返回字符串
     *
     * @param response
     * @param string
     * @return
     */
    protected String renderString(HttpServletResponse response, String string, String type) {
        return ResponseJsonUtil.renderString(response, string, type);
    }

    public JsonResultModel getJsonResultModel() {
        return jsonResultModel;
    }

    public void setJsonResultModel(JsonResultModel jsonResultModel) {
        this.jsonResultModel = jsonResultModel;
    }


    public String getJwtUserId() {
        return getRequest().getAttribute("jwtUserId") != null ? getRequest().getAttribute("jwtUserId").toString() : null;
    }

    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return attrs.getRequest();
    }

}
