package com.tujia.staff.service.user;


import com.tujia.staff.common.utils.SecurityUtil;
import com.tujia.staff.dao.user.UserMapper;
import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.user.User;
import com.tujia.staff.vo.attend.AttendQueryCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by haibingm 2018/01/11.
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;


    @Override
    public int updateByPrimaryKeySelective(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public User findUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     *@Author JackWang [www.employee.com]
     *@Date 2017/6/18 12:48
     *@Description 根据用户名查询用户
     */
    @Override
    public User findUserByUserName(String username) {
        User user =userMapper.selectByUserName(username);
        return user;
    }


    @Override
    public int deleteUserById(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        user.setPassword(SecurityUtil.encryptPassword(user.getPassword()));

        userMapper.insertSelective(user);
    }


    @Override
    public PageQueryBean listUser(AttendQueryCondition condition) {
        //根据条件查询 count记录数目
        int count = userMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            List<User> attendList =  userMapper.selectUserPage(condition);
            pageResult.setItems(attendList);
        }
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        return pageResult;
    }
}
