package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.RoleDao;
import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.dao.mapper.RoleRowMapper;
import com.impl.homesecurity.domain.enumeration.Role;
import com.impl.homesecurity.web.rest.errors.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by dima.
 * Creation date 23.10.18.
 */

@Repository
public class RoleDaoImp implements RoleDao {

    private final Logger log = LoggerFactory.getLogger(RoleDaoImp.class);
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    private final RoleRowMapper roleRowMapper;

    @Autowired
    public RoleDaoImp(@Qualifier("mysqlJdbcTemplateApp") JdbcTemplate jdbcTemplate, UserDao userDao, RoleRowMapper roleRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.roleRowMapper = roleRowMapper;
    }

    @Override
    public int addRole(Role role) {
        if (log.isDebugEnabled()) {
            log.debug("Try create role");
        }
        try {
            String sql = "INSERT INTO role (role) " +
                    "VALUES (?)";

            return jdbcTemplate.update(sql, role.getRoleName());
        } catch (DuplicateKeyException e) {
            if (log.isErrorEnabled()) {
                log.error("Error adding role, role =  " + role + "\n" + e.getMessage());
            }
            return 0;
        }
    }

    @Override
    public Role getRoleById(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("try get role with id = " + id);
        }
        String sql = "SELECT id, role FROM role WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[] {id}, roleRowMapper);
    }

    @Override
    public int updateRole(Role role) {
        if (log.isDebugEnabled()) {
            log.debug("try delete role = " + role + " with id = ");
        }
        String sql = "UPDATE role SET role = ? WHERE id = ?";
        return jdbcTemplate.update(sql, role);
    }

    @Override
    public int deleteRole(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Try delete role with id = " + id);
        }
        String sql = "DELETE FROM role WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Role getRoleByUser(long id) {
        if (log.isDebugEnabled()) {
            log.debug("try get role with user id = " + id);
        }
        if (userDao.getUserById(id) != null) {
            String sql = "SELECT user_id, device_id, `value` FROM settings WHERE user_id = ?";

            return jdbcTemplate.queryForObject(sql, roleRowMapper, id);
        } else {
            throw new BadRequestException("user with id = " + id + " does not exist");
        }
    }

    @Override
    public List<Role> getAllRoles() {
        if (log.isDebugEnabled()) {
            log.debug("Try get all roles");
        }
        String sql = "SELECT * FROM role";
        return jdbcTemplate.query(sql, roleRowMapper);
    }
}
