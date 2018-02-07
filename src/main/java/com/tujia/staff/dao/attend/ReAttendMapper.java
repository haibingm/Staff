package com.tujia.staff.dao.attend;


import com.tujia.staff.model.attend.ReAttend;
import com.tujia.staff.vo.attend.ReAttendQueryCondition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReAttendMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ReAttend record);

    int insertSelective(ReAttend record);

    ReAttend selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ReAttend record);

    int updateByPrimaryKey(ReAttend record);

    List<ReAttend> selectReAttendPage(ReAttendQueryCondition condition);

    int countByCondition(ReAttendQueryCondition condition);

}