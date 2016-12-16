package com.thinkgem.jeesite.restful.filter;

import com.google.common.base.Preconditions;
import com.thinkgem.jeesite.common.json.JsonResultModel;
import com.thinkgem.jeesite.common.json.ResponseJsonUtil;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.restful.Constants;
import com.thinkgem.jeesite.restful.realm.StatelessToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatelessAuthcFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        MyHttpServletRequestWrapper myRequest= (MyHttpServletRequestWrapper) request;

        //1.获取appId
        String appId=myRequest.getHeader(Constants.PARAM_APPID);
        //2.获取时间戳
        String strTimestamp=myRequest.getHeader(Constants.PARAM_TIMESTAMP);
        //3.获取nonce
        String nonce=myRequest.getHeader(Constants.PARAM_NONCE);
        //4.获取客户端生成的摘要信息
        String clientDigest=myRequest.getHeader(Constants.PARAM_DIGEST);

        try {
            Preconditions.checkArgument(!StringUtils.isBlank(appId), "请求头参数" + Constants.PARAM_APPID + "不能为空");
            Preconditions.checkArgument(!StringUtils.isBlank(strTimestamp), "请求头参数" + Constants.PARAM_TIMESTAMP + "不能为空");
            Preconditions.checkArgument(!StringUtils.isBlank(nonce), "请求头参数" + Constants.PARAM_NONCE + "不能为空");
            Preconditions.checkArgument(!StringUtils.isBlank(clientDigest), "请求头参数" + Constants.PARAM_DIGEST + "不能为空");

            //5.判断客户端传递的时间与当前时间差,大于1天不允许调用;
            Long timestamp= Long.parseLong(strTimestamp);
            if(Math.abs(DateUtils.getTimeStamps()/1000-timestamp)>Constants.PARAM_INTERVALTIME){
                onResponseFail(response,"method invoke attempted replay attack!");
                return false;
            }
        } catch (NumberFormatException e){
            e.printStackTrace();
            onResponseFail(response,"请求头参数"+Constants.PARAM_TIMESTAMP+"为时间戳格式");
            return false;
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            onResponseFail(response,e.getMessage());
            return false;
        }

        try{

            String contentType=request.getContentType();
            String uri=myRequest.getServletPath();
            if(!StringUtils.isBlank(myRequest.getQueryString()))
                uri+="?"+myRequest.getQueryString();

            StatelessToken token;
            //判断请求类型是否为json请求
            if(contentType!=null&&contentType.contains(MediaType.APPLICATION_JSON_VALUE)){
                String bodyJson=myRequest.getBody();
                token= new StatelessToken(appId,strTimestamp,nonce, null, clientDigest,bodyJson,uri);
            }else{
                Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());
                token= new StatelessToken(appId,strTimestamp,nonce, params, clientDigest,null,uri);
            }

            //7、委托给Realm进行登录
            getSubject(request, response).login(token);

        }  /*catch (UnknownAccountException e){
            e.printStackTrace();
            onResponseFail(response,e.getMessage());
            return false;
        } catch (DisabledAccountException e){
            e.printStackTrace();
            onResponseFail(response,e.getMessage());
            return false;
        } */catch (AuthenticationException e){
            e.printStackTrace();
            onResponseFail(response,"login error");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            onResponseFail(response,"login error");//6、登录失败
            return false;
        }
        return true;
    }

    //反馈异常信息给调用端
    private void onResponseFail(ServletResponse response,String message) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonResultModel jsonResultModel=new JsonResultModel();
        jsonResultModel.setMessage(message);
        jsonResultModel.setStateError();

        ResponseJsonUtil.renderString(httpResponse,jsonResultModel);
    }

}
