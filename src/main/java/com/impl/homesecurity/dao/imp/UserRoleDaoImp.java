package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.UserRoleDao;
import com.impl.homesecurity.domain.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by vyacheslav on 25.10.18.
 */
@Repository
public class UserRoleDaoImp implements UserRoleDao {

    private final Logger log = LoggerFactory.getLogger(UserRoleDaoImp.class);
    private final JdbcTemplate jdbcTemplate;

    public UserRoleDaoImp(@Qualifier("mysqlJdbcTemplateApp") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addUserRole(UserRole userRole) {
        if (log.isDebugEnabled()) {
            log.debug("Try adding Role to User");
        }
        try {
            String sql = "INSERT INTO users_role (user_id, role_id) VALUES (?, ?)";
            return jdbcTemplate.update(sql, userRole.getUserId(), (userRole.getRole().getCustomOrdinal()));
        } catch (DuplicateKeyException e) {
            if (log.isErrorEnabled()) {
                log.error("Error adding Role " + userRole.getRole() + " to UserID =  " + userRole.getUserId() + "\n" + e.getMessage());
            }
            return 0;
        }
    }

    @Override
    public int deleteUserRole(Long userId) {
        if (log.isDebugEnabled()) {
            log.debug("Try delete userRole");
        }
        String sql = "DELETE FROM users_role WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}
