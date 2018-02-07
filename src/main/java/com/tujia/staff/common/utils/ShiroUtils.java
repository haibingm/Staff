package com.tujia.staff.common.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * shiro工具类
 * Created by haibingm on 2018/1/16.
 */
public class ShiroUtils {

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static Session getSession(){
        return getSubject().getSession();
    }

    public static Object getSessionByName(String sessionName){
        return  getSession().getAttribute(sessionName);
    }

    public static void logout(){
        getSession().removeAttribute(Const.SESSION_USER);
        getSubject().logout();
    }
}
