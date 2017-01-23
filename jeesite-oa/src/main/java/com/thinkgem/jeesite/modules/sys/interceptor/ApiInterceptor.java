package com.thinkgem.jeesite.modules.sys.interceptor;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.JwtTokenUtils;
import org.json.simple.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ApiInterceptor extends BaseService implements HandlerInterceptor {

    public static final String[] PUBLIC_URIS = {"(.*)/api/login/(.*)"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String uri = request.getRequestURI();
        for (String publicUri : PUBLIC_URIS) {
            if (uri.matches(publicUri)) {
                return true;
            }
        }
        String token = request.getHeader("token");
        String userId = JwtTokenUtils.getUserIdByToken(token);
        if (StringUtils.isBlank(userId)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            Map map = new HashMap();
            map.put("state", -1);
            map.put("message", "please log in");
            map.put("data", "");
            response.getWriter().append(JSONObject.toJSONString(map));
            return false;
        } else {
            request.setAttribute("jwtUserId", userId);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }

}
