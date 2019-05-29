package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.UserRole;

/**
 * Created by vyacheslav on 24.10.18.
 */
public interface UserRoleDao {

    int addUserRole(UserRole userRole);
    int deleteUserRole(Long userId);
}
