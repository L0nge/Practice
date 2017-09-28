package com.my.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Longe on 2017.09.28.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
//    private static final Logger LOGGER= LoggerFactory.getLogger(WXLoginInterceptor.class);
    private List<String> excludeUrls;

    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        Boolean result=true;
        String requestUri = request.getRequestURI();
//        LOGGER.info("WXVisitInterceptor preHandle requestUri="+requestUri);
        for (String url : excludeUrls) {
            if (requestUri.endsWith(url)) {
                return true;
            }
        }
//        Cookie cookie = CookieUtil.getCookieByName(request, "wxVisit");
//        if (cookie == null || "".equals(cookie.getValue())) {
//            String requestUrl=request.getRequestURL().toString();
//            StringBuffer requestParam= StringUtil.getRequestParam(request);
//
//            request.setAttribute("returnUrl",requestUrl+requestParam.toString());
//            LOGGER.info("WXVisitinterceptor returnUrl:"+requestUrl+requestParam.toString());
//
//            request.getRequestDispatcher("/wxlogin/etranceOnlyOpenId.jhtml").forward(request, response);
//
//            result = false;
//        }
        return result;
    }
}
