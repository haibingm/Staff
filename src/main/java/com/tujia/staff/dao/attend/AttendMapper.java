package com.tujia.staff.dao.attend;


import com.tujia.staff.model.attend.Attend;
import com.tujia.staff.vo.attend.AttendQueryCondition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Attend record);

    int batchInsert(List<Attend> attendList);

    int insertSelective(Attend record);

    Attend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attend record);

    int updateByPrimaryKey(Attend record);

    Attend selectTodaySignRecord(Long userId);

    List<Long> selectTodayAbsence();

    List<Attend> selectTodayEveningAbsence();

    List<Attend> selectAllAttend();

    int countByCondition(AttendQueryCondition condition);

    List<Attend> selectAttendPage(AttendQueryCondition condition);
}