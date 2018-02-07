package com.tujia.staff.controller.attend;


import com.tujia.staff.common.utils.Const;
import com.tujia.staff.common.utils.ShiroUtils;
import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.attend.Attend;
import com.tujia.staff.model.user.User;
import com.tujia.staff.service.attend.AttendService;
import com.tujia.staff.vo.attend.AttendQueryCondition;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by haibingm 2018/01/11
 */
@Controller
@RequestMapping("attend")
public class AttendController {

    @Autowired
    private AttendService attendService;

    @RequestMapping
    public String toAttend(){

        return "attend/attend";
    }

    /**
     *@Author haibingm
     *@Date 2018/01/15
     *@Description 检查是否当天有签到记录
     */
    @RequestMapping("/checkSign")
    @ResponseBody
    public String checkSign() throws Exception{
        User user = (User) ShiroUtils.getSessionByName(Const.SESSION_USER);
        Attend attend = attendService.checkTodaySign(user.getId());
        if(attend == null ||(attend.getAttendMorning() == null && attend.getAttendEvening() == null)){
            return "no";
        }else if(attend.getAttendMorning()!=null){
            return "yes";
        }
        return "yes";
    }

    /**
     *@Author haibingm
     *@Date 2018/01/11
     *@Description 签到
     */
    @RequestMapping("/sign")
    @ResponseBody
    public String signAttend() throws Exception {
        User user = (User) ShiroUtils.getSessionByName(Const.SESSION_USER);
        Attend attend = new Attend();
        attend.setUserId(user.getId());
        attendService.signAttend(attend);
        return "succ";
    }

    /**
     *@Author haibingm
     *@Date 2018/01/11
     *@Description 签到API
     */
    @RequestMapping("/signAPI")
    @ResponseBody
    public String signAttendAPI(@RequestBody Attend attend) throws Exception {
        User user = (User) ShiroUtils.getSessionByName(Const.SESSION_USER);
        attend.setUserId(user.getId());
        attendService.signAttend(attend);
        return "succ";
    }

    /**
     *@Author haibingm
     *@Date 2018/01/11
     *@Description  考勤数据分页查询
     */
//    @RequiresRoles("leader")
    @RequiresPermissions("attend:attendList")
    @RequestMapping("/attendList")
    @ResponseBody
    public PageQueryBean listAttend(AttendQueryCondition condition){
        User user = (User) ShiroUtils.getSessionByName(Const.SESSION_USER);
        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUserId(user.getId());
        PageQueryBean result = attendService.listAttend(condition);
        return result;
    }

    @RequestMapping("/goReAttend")
    public ModelAndView goEdit(ModelAndView mv, HttpServletRequest request){
        Long id = Long.parseLong(request.getParameter("id"));
        Attend attend = attendService.findAttendById(id);
        mv.addObject("attend",attend);
        mv.setViewName("attend/re_attend_dialog");
        return mv;
    }
}
