package com.tujia.staff.common.interceptor;

import com.tujia.staff.common.utils.Const;
import com.tujia.staff.model.user.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by haibingm 2018/01/11.
 */
public class SessionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String path = request.getServletPath();
        if((path.matches(Const.NO_INTERCEPTOR_PATH))){
            return true;
        }
        //shiro管理的session
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        if(user!=null){
            return true;
        }
         //转发到登录
        request.getRequestDispatcher(Const.LOGIN).forward(request,response);
        return false;
    }
}
