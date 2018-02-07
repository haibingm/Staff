package com.tujia.staff.service.attend;


import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.attend.Attend;
import com.tujia.staff.vo.attend.AttendQueryCondition;

/**
 * Created by JackWangon[www.aiprogram.top] 2017/6/20.
 */
public interface AttendService {

    void signAttend(Attend attend) throws Exception;

    PageQueryBean listAttend(AttendQueryCondition condition);

    Attend checkTodaySign(Long userId);

    Attend findAttendById(Long id);

    int updateBySelectKey(Attend attend);
}
