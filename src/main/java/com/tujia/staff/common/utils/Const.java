package com.tujia.staff.common.utils;

/**
 * Created by haibingm on 2018/1/11.
 */
public class Const {
    public static final String SESSION_SECURITY_CODE = "sessionSecCode";
    public static final String SESSION_USER = "sessionUser";
    public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";
    public static final String SESSION_allmenuList = "allmenuList";		//全部菜单
    public static final String SESSION_QX = "QX";
    public static final String LOGIN = "/login";				//登录地址
    public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(logout)|(sign)|(error)).*";	//不对匹配该值的访问路径拦截（正则）

    /**
     * 补签流程相关状态
     */
    public static final int ABSENCE_DAY = 540;
    public static final Byte ATTEND_STATUS_ABNORMAL = 2;
    public static final Byte RE_ATTEND_STATUS_ONGOING =1 ;
    public static final Byte RE_ATTEND_STATUS_PASS =2 ;
    public static final Byte RE_ATTEND_STATUS_REFUSE =3 ;
    public static final Byte ATTEND_STATUS_NORMAL =1 ;
    public static final Byte ATTEND_STATUS_REATTEND = 3;
    public static final String NEXT_HANDLER = "next_handler";
    public static final String RE_ATTEND_SIGN = "re_attend";
    public static final String RE_ATTEND_FLOW_ID = "re_attend";

}
