package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.RoleDao;
import com.impl.homesecurity.domain.enumeration.Role;
import com.impl.homesecurity.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by dima.
 * Creation date 27.11.18.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);
    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> getAllRoles() {
        if (log.isDebugEnabled()) {
            log.debug("Request to get all roles");
        }
        return roleDao.getAllRoles();
    }

    @Override
    public Role getRoleById(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get Role with id {}", id);
        }
        return roleDao.getRoleById(id);
    }
}
