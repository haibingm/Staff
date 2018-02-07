package com.tujia.staff.common.task;


import com.tujia.staff.common.utils.Const;
import com.tujia.staff.common.utils.DateUtils;
import com.tujia.staff.dao.attend.AttendMapper;
import com.tujia.staff.model.attend.Attend;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JackWangon[www.aiprogram.top] 2017/6/25.
 */
public class AttendCheckTask {


    @Autowired
    private AttendMapper attendMapper;


    public void checkAttend() {
        //查询缺勤用户ID插入打卡记录，并记缺勤540分钟
        List<Long> userIdList = attendMapper.selectTodayAbsence();
        if(CollectionUtils.isNotEmpty(userIdList)){
            List<Attend> attendList = new ArrayList<>();
            for(Long userId : userIdList){
                Attend attend = new Attend();
                attend.setUserId(userId);
                attend.setAttendDate(new Date());
                attend.setAttendWeek((byte) DateUtils.getTodayWeek());
                attend.setWorkTime(0);
                attend.setAbsence(Const.ABSENCE_DAY);
                attend.setAttendStatus(Const.ATTEND_STATUS_ABNORMAL);
                attendList.add(attend);
            }
            attendMapper.batchInsert(attendList);
        }
        //检查晚上未打卡员工设置为异常,缺勤540分钟
        List<Attend> absenceList = attendMapper.selectTodayEveningAbsence();
        if(CollectionUtils.isNotEmpty(absenceList)){
            for(Attend attend : absenceList){
                attend.setAbsence(Const.ABSENCE_DAY);
                attend.setAttendStatus(Const.ATTEND_STATUS_ABNORMAL);
                attend.setWorkTime(0);
                attendMapper.updateByPrimaryKeySelective(attend);
            }
        }

        //检查是否满9个小时，未满记录缺勤时间
        List<Attend> attendList = attendMapper.selectAllAttend();
        for(Attend attend : attendList){
            int work_time = DateUtils.getMunite(attend.getAttendMorning(),attend.getAttendEvening());
            attend.setWorkTime(work_time);
            if(work_time<Const.ABSENCE_DAY){
                attend.setAbsence(Const.ABSENCE_DAY-work_time);
                attend.setAttendStatus(Const.ATTEND_STATUS_ABNORMAL);
            }else {
                attend.setAbsence(0);
                attend.setAttendStatus(Const.ATTEND_STATUS_NORMAL);
            }
            attendMapper.updateByPrimaryKeySelective(attend);
        }
    }
}
