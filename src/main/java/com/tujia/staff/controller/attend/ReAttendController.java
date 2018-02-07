package com.tujia.staff.controller.attend;


import com.tujia.staff.common.utils.Const;
import com.tujia.staff.common.utils.ShiroUtils;
import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.attend.Attend;
import com.tujia.staff.model.attend.ReAttend;
import com.tujia.staff.model.user.User;
import com.tujia.staff.service.attend.AttendService;
import com.tujia.staff.service.attend.ReAttendService;
import com.tujia.staff.vo.attend.AttendVo;
import com.tujia.staff.vo.attend.ReAttendQueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by haibingm on 2018/1/17.
 */
@RequestMapping("reAttend")
@Controller
public class ReAttendController {

    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
    DateFormat timeFmt =new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ReAttendService reAttendService;

    @Autowired
    private AttendService attendService;

    @RequestMapping
    public String toReAttend(){
        return "attend/re_attend";
    }

    /**
     *@Author haibingm
     *@Date 2018/01/17
     *@Description  考勤补签数据分页查询
     */
//    @RequiresRoles("leader")
    @RequestMapping("/reAttendList")
    @ResponseBody
    public PageQueryBean listReAttend(ReAttendQueryCondition condition){
        User user = (User) ShiroUtils.getSessionByName(Const.SESSION_USER);
        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUsername(user.getUsername());
        PageQueryBean result = reAttendService.listReAttend(condition);
        return result;
    }

    @RequestMapping(value = "/startReAttend",method = RequestMethod.POST)
    public String startReAttend(@RequestBody AttendVo attendVo) throws Exception{
        User user = (User)ShiroUtils.getSessionByName(Const.SESSION_USER);
        Attend attend = attendService.findAttendById(Long.parseLong(attendVo.getAttendId()));
        attend.setAttendStatus(Const.ATTEND_STATUS_REATTEND);
        attendService.updateBySelectKey(attend);
        ReAttend reAttend = new ReAttend();
        reAttend.setReAttendStarter("laowang666");
        reAttend.setAttendId(Long.parseLong(attendVo.getAttendId()));
        reAttend.setAttendDate(fmt.parse(attendVo.getAttendDate()));
        reAttend.setReAttendMor(timeFmt.parse(attendVo.getReAttendMor()));
        reAttend.setReAttendEve(timeFmt.parse(attendVo.getReAttendEve()));
        reAttend.setComments(attendVo.getComments());
        reAttendService.startReAttendFlow(reAttend);
        return "succ";
    }

    @RequestMapping("/approve")
    public String toApprove(){
        return "attend/re_attend_approve";
    }

    @RequestMapping("/approve/list")
    @ResponseBody
    public PageQueryBean listApprove(ReAttendQueryCondition condition) throws Exception{
        User user = (User)ShiroUtils.getSessionByName(Const.SESSION_USER);
        String [] rangeDate = condition.getRangeDate().split("/");
        condition.setStartDate(rangeDate[0]);
        condition.setEndDate(rangeDate[1]);
        condition.setUsername(user.getUsername());
        PageQueryBean tasks = reAttendService.listTasks(condition);
        return tasks;
    }

    @RequestMapping(value = "/approve/startApprove",method = RequestMethod.POST)
    @ResponseBody
    public String startApprove(@RequestBody AttendVo attendVo) throws Exception{
        ReAttend reAttend = new ReAttend();
        reAttend.setId(Long.parseLong(attendVo.getId()));
        reAttend.setTaskId(attendVo.getTaskId());
        reAttend.setAttendId(Long.parseLong(attendVo.getAttendId()));
        reAttend.setReAttendMor(timeFmt.parse(timeFmt.format(Long.parseLong(attendVo.getReAttendMor()))));
        reAttend.setReAttendEve(timeFmt.parse(timeFmt.format(Long.parseLong(attendVo.getReAttendEve()))));
        reAttend.setApproveFlag(attendVo.getApproveFlag());
        reAttendService.reAttendApprove(reAttend);
        return "succ";
    }
}
