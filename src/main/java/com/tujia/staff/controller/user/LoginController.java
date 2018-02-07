package com.tujia.staff.controller.user;


import com.tujia.staff.model.user.User;
import com.tujia.staff.service.user.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JackWangon[www.aiprogram.top] 2017/6/17.
 */
@Controller
@RequestMapping("login")
public class LoginController {


    private static String ERROR_INFO = "";
    @Autowired
    private UserService userService;

    /**
     *@Author JackWang [www.employee.com]
     *@Date 2017/6/18 12:47
     *@Description  登录页面
     */
    @RequestMapping
    public String login(){
        return "login";
    }


    /**
     *@Author JackWang [www.employee.com]
     *@Date 2017/6/18 12:47
     *@Description 校验登录
     */
    @RequestMapping("/check")
    @ResponseBody
    public String checkLogin(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String username = request.getParameter("username");
        String pwd=request.getParameter("password");
        if(username!=""&&pwd!=""&&username!=null&&pwd!=null){
            UsernamePasswordToken token = new UsernamePasswordToken(username,pwd);
            Subject subject = SecurityUtils.getSubject();
            try {
                subject.login(token);
                ERROR_INFO = "login_succ";
            }catch (Exception e){
                return "usererror";
            }
        }else {
            ERROR_INFO = "error";
        }
       return ERROR_INFO;
    }


    @RequestMapping("/register")
    @ResponseBody
    public String register(@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        userService.createUser(user);

        return "succ";
    }
}
