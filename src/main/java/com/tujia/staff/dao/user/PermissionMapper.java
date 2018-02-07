package com.tujia.staff.dao.user;


import com.tujia.staff.model.user.Permission;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionMapper {
    int deleteByPrimaryKey(Long permissionid);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long permissionid);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);
}