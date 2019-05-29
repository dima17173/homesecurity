package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.UserDeviceDao;
import com.impl.homesecurity.dao.mapper.UserRoleDevicesRowMapper;
import com.impl.homesecurity.domain.UserDevice;
import com.impl.homesecurity.domain.UserRoleDevices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UserDeviceDaoImpl implements UserDeviceDao {

    private final Logger log = LoggerFactory.getLogger(UserDeviceDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private UserRoleDevicesRowMapper userRoleDevicesRowMapper;

    public UserDeviceDaoImpl(@Qualifier("mysqlJdbcTemplateApp") JdbcTemplate jdbcTemplate,
                             UserRoleDevicesRowMapper userRoleDevicesRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRoleDevicesRowMapper = userRoleDevicesRowMapper;
    }

    @Override
    public int addUserDevice(UserDevice userDevice) {
        if (log.isDebugEnabled()) {
            log.debug("Try add UserDevice");
        }
        try {
            String sql = "INSERT INTO users_device (user_id, device_id) VALUES (?, ?)";

            return jdbcTemplate.update(sql, userDevice.getUserId(), userDevice.getDeviceId());
        } catch (DuplicateKeyException e) {
            if (log.isErrorEnabled()) {
                log.error("Error adding Device " + userDevice.getDeviceId() + " to UserID =  " + userDevice.getUserId() + "\n" + e.getMessage());
            }
            return 0;
        }
    }

    @Override
    public Optional<UserRoleDevices> getUserRoleDevicesByUserId(long id){
        if (log.isDebugEnabled()) {
            log.debug("Try get UserRole with login = " + id);
        }
        try {
            String sql ="SELECT u.id, u.login, u.password, u.name, u.first_sign_in, u.main_users_id, r.role_id, de.id AS device_id, de.device_name, de.device_number, de.status, de.hex_name " +
                    "from user as u INNER JOIN users_device AS d ON d.user_id = u.id " +
                    "               INNER JOIN device de on de.id = d.device_id " +
                    "               INNER JOIN users_role as r on u.id = r.user_id " +
                    "WHERE u.id = ?";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRoleDevicesRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("Error getting user " + "\n" + e.getMessage());
            }
            return Optional.empty();
        }
    }

    @Override
    public int deleteUserDevice(Long userId) {
        if (log.isDebugEnabled()) {
            log.debug("Try delete user device with user id = " + userId);
        }
        String sql = "DELETE FROM users_device WHERE user_id = ?";

        return jdbcTemplate.update(sql, userId);
    }
}
