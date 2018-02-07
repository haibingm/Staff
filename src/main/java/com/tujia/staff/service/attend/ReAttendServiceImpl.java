package com.tujia.staff.service.attend;


import com.tujia.staff.common.utils.Const;
import com.tujia.staff.common.utils.DateUtils;
import com.tujia.staff.dao.attend.AttendMapper;
import com.tujia.staff.dao.attend.ReAttendMapper;
import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.attend.Attend;
import com.tujia.staff.model.attend.ReAttend;
import com.tujia.staff.vo.attend.ReAttendQueryCondition;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haibingm on 2018/1/17.
 */
@Service
public class ReAttendServiceImpl implements ReAttendService{

    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private ReAttendMapper reAttendMapper;

    @Autowired
    private AttendMapper attendMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    //开启补签流程
    @Override
    public void startReAttendFlow(ReAttend reAttend) {
        //查询到上级领导用户名
        reAttend.setCurrentHandler("bingo");
        reAttend.setStatus(Const.RE_ATTEND_STATUS_ONGOING);
        //插入补签表
        reAttendMapper.insertSelective(reAttend);
        Map<String,Object>map = new HashMap<>();
        map.put(Const.RE_ATTEND_SIGN,reAttend);
        map.put(Const.NEXT_HANDLER,reAttend.getCurrentHandler());
        //启动补签流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(Const.RE_ATTEND_FLOW_ID,map);
        //提交用户补签任务
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        taskService.complete(task.getId(),map);
    }

    //根据流程任务变量查询领导需要处理的任务
    @Override
    public PageQueryBean listTasks(ReAttendQueryCondition condition) throws Exception{

        List<ReAttend> reAttendList = new ArrayList<>();
        List<Task> taskList = taskService.createTaskQuery().processVariableValueEquals(condition.getUsername()).list();
        PageQueryBean pageResult = new PageQueryBean();
        if(CollectionUtils.isNotEmpty(taskList)){
            pageResult.setTotalRows(taskList.size());
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            for(Task task : taskList){
                Map<String,Object> variable = taskService.getVariables(task.getId());
                ReAttend reAttend = (ReAttend)variable.get(Const.RE_ATTEND_SIGN);
                if(reAttend.getAttendDate().getTime() >= fmt.parse(condition.getStartDate()).getTime()
                        && reAttend.getAttendDate().getTime() <= fmt.parse(condition.getEndDate()).getTime()){
                    reAttend.setTaskId(task.getId());
                    reAttendList.add(reAttend);
                }
            }
            pageResult.setItems(reAttendList);
        }
        return pageResult;
    }

    //员工查询补签申请状态
    @Override
    public PageQueryBean listReAttend(ReAttendQueryCondition condition) {
        int count = reAttendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            List<ReAttend> attendList =  reAttendMapper.selectReAttendPage(condition);
            pageResult.setItems(attendList);
        }
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        return pageResult;
    }

    //审批工作流
    @Override
    public void reAttendApprove(ReAttend reAttend) {
        Task task = taskService.createTaskQuery().taskId(reAttend.getTaskId()).singleResult();
        if((Const.RE_ATTEND_STATUS_PASS + "").equals(reAttend.getApproveFlag())){
            //审批通过，修改补签数据状态并将考勤状态改为正常
            Attend attend = new Attend();
            attend.setId(reAttend.getAttendId());
            attend.setAttendStatus(Const.ATTEND_STATUS_NORMAL);
            attend.setAttendMorning(reAttend.getReAttendMor());
            attend.setAttendEvening(reAttend.getReAttendEve());
            int work_time = DateUtils.getMunite(attend.getAttendMorning(),attend.getAttendEvening());
            attend.setWorkTime(work_time);
            attend.setAbsence(0);
            reAttend.setStatus(Const.RE_ATTEND_STATUS_PASS);
            attendMapper.updateByPrimaryKeySelective(attend);
            reAttendMapper.updateByPrimaryKeySelective(reAttend);
        }else if ((Const.RE_ATTEND_STATUS_REFUSE + "").equals(reAttend.getApproveFlag())){
            reAttend.setStatus(Const.RE_ATTEND_STATUS_REFUSE);
            reAttendMapper.updateByPrimaryKeySelective(reAttend);
        }
        taskService.complete(reAttend.getTaskId());
    }
}
