package com.tujia.staff.controller.user;

import com.tujia.staff.common.utils.Const;
import com.tujia.staff.common.utils.ShiroUtils;
import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.user.User;
import com.tujia.staff.service.user.UserService;
import com.tujia.staff.vo.attend.AttendQueryCondition;
import com.tujia.staff.vo.user.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * Created by haibingm 2018/01/11
 */
@Controller
@RequestMapping("user")
public class UserController {

    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟

    @Autowired
    private UserService userService;

    @RequestMapping
    public String home(){
     return "user/user";
    }

    /**
     *@Author haibingm
     *@Date 2018/01/11
     *@Description  获取用户信息
     */
    @RequestMapping("/userinfo")
    @ResponseBody
    public User getUser(){
       return (User) ShiroUtils.getSessionByName(Const.SESSION_USER);
    }


    /**
     *@Author haibingm
     *@Date 2018/01/11
     *@Description 登出系统
     */
    @RequestMapping("/logout")
    public String logout(){
        //shiro管理的session
        ShiroUtils.logout();
        return "login";
    }

    /**
     *@Author haibingm
     *@Date 2018/01/11
     *@Description  用户数据分页查询
     */
    @RequiresRoles("leader")
    @RequestMapping("/userList")
    @ResponseBody
    public PageQueryBean listUser(AttendQueryCondition condition){
        User user = (User) ShiroUtils.getSessionByName(Const.SESSION_USER);
        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUserId(user.getId());
        PageQueryBean result = userService.listUser(condition);
        return result;
    }

    @RequestMapping("/goEdit")
    public ModelAndView goEdit(ModelAndView mv, HttpServletRequest request){
        Long id = Long.parseLong(request.getParameter("id"));
        String action = request.getParameter("act");
        User user = userService.findUserById(id);
        mv.addObject("user",user);
        mv.addObject("action",action);
        mv.setViewName("user/user_edit");
        return mv;
    }

    @RequiresPermissions("user:edit")
    @RequestMapping("/editUser")
    @ResponseBody
    public String editUser(UserVo user) throws Exception{
        User usr = userService.findUserByUserName(user.getUsername());
        usr.setRealName(user.getRealName());
        usr.setMobile(user.getMobile());
        usr.setEntryTime(sdf.parse(user.getEntryTime()));
        int count = userService.updateByPrimaryKeySelective(usr);
        if(count>0){
            return "succ";
        }else {
            return "error";
        }
    }

    @RequiresPermissions("user:delete")
    @RequestMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(HttpServletRequest request) throws Exception{
        Long id = Long.parseLong(request.getParameter("id"));
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        User user = (User) session.getAttribute(Const.SESSION_USER);
        if(id != user.getId()){
            int count = userService.deleteUserById(id);
            if(count>0){
                return "succ";
            }else {
                return "error";
            }
        }else {
            return "error2";
        }
    }
}
