package com.impl.homesecurity.service;

import com.impl.homesecurity.domain.enumeration.Role;
import java.util.List;

/**
 * Created by dima.
 * Creation date 27.11.18.
 */
public interface RoleService {
    List <Role>getAllRoles();
    Role getRoleById(Long id);
}
