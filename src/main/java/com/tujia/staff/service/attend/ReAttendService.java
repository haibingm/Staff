package com.tujia.staff.service.attend;


import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.attend.ReAttend;
import com.tujia.staff.vo.attend.ReAttendQueryCondition;

/**
 * Created by haibingm on 2018/1/17.
 */
public interface ReAttendService {

    void startReAttendFlow(ReAttend reAttend); //发起补签流程

    PageQueryBean listTasks(ReAttendQueryCondition condition) throws Exception;//领导补签审批任务列表

    void reAttendApprove(ReAttend reAttend);//补签审批流程

    PageQueryBean listReAttend(ReAttendQueryCondition condition);//员工补签列表
}
