package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.enumeration.Role;
import java.util.List;

/**
 * Created by dima.
 * Creation date 23.10.18.
 */
public interface RoleDao {

    int addRole(Role role);
    Role getRoleById(Long id);
    int updateRole(Role role);
    int deleteRole(long id);
    List<Role> getAllRoles();
    Role getRoleByUser(long id);
}
