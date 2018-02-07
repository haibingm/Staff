package com.tujia.staff.service.user;


import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.user.User;
import com.tujia.staff.vo.attend.AttendQueryCondition;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by haibingn 2018/01/11.
 */
public interface UserService {

    User findUserByUserName(String username);

    User findUserById(Long id);

    void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;

    int updateByPrimaryKeySelective(User user);

    PageQueryBean listUser(AttendQueryCondition condition);

    int deleteUserById(Long id);
}
