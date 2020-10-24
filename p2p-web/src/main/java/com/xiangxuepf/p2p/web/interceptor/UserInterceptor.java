package com.xiangxuepf.p2p.web.interceptor;

import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.exterface.model.user.User;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mhw
 */
public class UserInterceptor implements org.springframework.web.servlet.HandlerInterceptor{


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //session中获取用户信息
        User sessionUser = (User) httpServletRequest.getSession().getAttribute(Constants.SESSION_USER);
        //判断用户是否登录
        if (null == sessionUser){
            //跳转至登录页面
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login.jsp");
            return false;
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
