package com.tujia.staff.dao.user;


import com.tujia.staff.model.user.User;
import com.tujia.staff.vo.attend.AttendQueryCondition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUserName(String username);

    int countByCondition(AttendQueryCondition condition);

    List<User> selectUserPage(AttendQueryCondition condition);}