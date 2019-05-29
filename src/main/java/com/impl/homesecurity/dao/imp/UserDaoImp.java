package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.dao.mapper.UserRoleDevicesRowMapper;
import com.impl.homesecurity.dao.mapper.UserRowMapper;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.UserRoleDevices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements UserDao {

    private final Logger log = LoggerFactory.getLogger(UserDaoImp.class);
    private final JdbcTemplate jdbcTemplate;
    private final UserRoleDevicesRowMapper rowMapperUserRoleDevices;
    private final UserRowMapper userRowMapper;

    @Autowired
    public UserDaoImp(@Qualifier("mysqlJdbcTemplateApp") JdbcTemplate jdbcTemplate, UserRoleDevicesRowMapper rowMapperUserRoleDevices, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapperUserRoleDevices = rowMapperUserRoleDevices;
        this.userRowMapper = userRowMapper;
    }

    @Override
    public int addUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("Try adding new user to DB");
        }
        try {
            String sql = "INSERT INTO user (login, password, name,  main_users_id, role_id)" +
                        " VALUES (?, ?, ?, ?, ?)";

            return jdbcTemplate.update(sql, user.getLogin(), user.getPassword(), user.getName(),
                    user.getMainUser().getId(),
                    user.getRole().getCustomOrdinal());
        } catch (DuplicateKeyException e) {
            if (log.isErrorEnabled()) {
                log.error("Error adding user, user =  " + user + "\n" + e.getMessage());
            }
            return 0;
        }
    }

    @Override
    public int deleteUser(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Try delete user with id = " + id);
        }
        String sql = "DELETE FROM user WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    @Override
    public User getUserById(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Try get user with id = " + id);
        }
        String sql = "SELECT id, login, password, name, first_sign_in, main_users_id, role_id " +
                "FROM user WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    @Override
    public User getUserByLogin(String login) {
        if (log.isDebugEnabled()) {
            log.debug("Try get user with login = " + login);
        }
        String sql = "SELECT id, login, password, name, first_sign_in, main_users_id, role_id " +
                "FROM user WHERE login = ?";

        return jdbcTemplate.queryForObject(sql, userRowMapper, login);
    }

    @Override
    public Optional<UserRoleDevices> getUserByIdWithRole(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Try get UserRole with id = " + id);
        }
        try {
            String sql ="SELECT u.id, u.login, u.password, u.name, u.first_sign_in, u.main_users_id, r.role_id " +
                    "from user as u " +
                    "INNER JOIN users_role as r on u.id = r.user_id " +
                    "WHERE u.id = ?";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapperUserRoleDevices, id));
        } catch (EmptyResultDataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("Error getting user " + "\n" + e.getMessage());
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllUsersByMainUser(long mainUserId){
        if (log.isDebugEnabled()) {
            log.debug("Try get users with id = " + mainUserId);
        }
        String sql = "SELECT id, login, password, name, first_sign_in, main_users_id, role_id" +
                " FROM user WHERE main_users_id = ?";

        return jdbcTemplate.query(sql, userRowMapper, mainUserId);
    }

    @Override
    public int updateUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("Try update user with id = " + user.getId());
        }
        String sql = "UPDATE user SET login = ?, password = ?, name = ?, first_sign_in = ?, " +
                "main_users_id = ?, role_id = ? " +
                "WHERE id = ?";

        return jdbcTemplate.update(sql, user.getLogin(), user.getPassword(), user.getName(),
                user.getFirstSignIn(), user.getMainUser().getId() == 0 ? null : user.getMainUser().getId(),
                user.getRole().getCustomOrdinal(), user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        if (log.isDebugEnabled()) {
            log.debug("Try get all users");
        }
        String sql = "SELECT id, login, password, name, first_sign_in, main_users_id, role_id FROM user";

        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public List<User> getAllUsersByRoleId(long roleId) {
        if (log.isDebugEnabled()) {
            log.debug("Try get all users by role id = " + roleId);
        }
        String sql = "SELECT u.id, u.login, u.password, u.name, u.first_sign_in, u.main_users_id, u.role_id " +
                "FROM user AS u " +
                "INNER JOIN users_role AS r ON u.id = r.user_id " +
                "WHERE u.role_id = ?";
        return jdbcTemplate.query(sql, userRowMapper, roleId);
    }

    @Override
    public Optional<UserRoleDevices> getUserRoleByLogin(String login) {
        if (log.isDebugEnabled()) {
            log.debug("Try get UserRole with login = " + login);
        }
        try {
            String sql ="SELECT u.id, u.login, u.password, u.name, u.first_sign_in, u.main_users_id, r.role_id " +
                    "from user as u " +
                    "INNER JOIN users_role as r on u.id = r.user_id " +
                    "WHERE u.login = ?";

            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapperUserRoleDevices, login));
        } catch (EmptyResultDataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("Error getting user " + "\n" + e.getMessage());
            }
            return Optional.empty();
        }
    }

    @Override
    public List<UserRoleDevices> getAllUserRoleDevices() {
        if (log.isDebugEnabled()) {
            log.debug("Try get all UserRoleDevices");
        }
        String sql ="SELECT u.id, u.login, u.password, u.name, u.first_sign_in, u.main_users_id, r.role_id, de.id AS device_id, de.device_name, " +
                "de.device_number, de.status, de.hex_name from user as u " +
                "INNER JOIN users_device AS d ON d.user_id = u.id " +
                "INNER JOIN device de on d.device_id = de.id " +
                "INNER JOIN users_role as r on u.id = r.user_id";

        return jdbcTemplate.query(sql ,rowMapperUserRoleDevices);
    }

    @Override
    public List<User> getUsersByDeviceId(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Try get all UserRoleDevices with user id = " + id);
        }
        String sql ="SELECT u.id, u.login, u.password, u.name, u.first_sign_in, u.main_users_id, r.role_id FROM user AS u " +
                "INNER JOIN users_device AS de ON u.id = de.user_id " +
                "INNER JOIN device d on de.device_id = d.id " +
                "INNER JOIN users_role as r on u.id = r.user_id " +
                "WHERE d.id = ?;";
        return jdbcTemplate.query(sql, userRowMapper, id);
    }

    @Override//todo не работает rowMapper, переделать если нужно будет использовать
    public List<UserRoleDevices> getUserRoleDevicesByDeviceId(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Try get UserRoleDevices with device id = " + id);
        }
        try {
            String sql ="SELECT u.id, u.login, u.password, u.name, u.first_sign_in, u.main_users_id, r.role_id, de.id AS device_id, de.device_name, " +
                    "de.device_number, de.hex_name, de.status from user as u " +
                    "INNER JOIN users_device AS d ON d.user_id = u.id " +
                    "INNER JOIN device de on d.device_id = de.id " +
                    "INNER JOIN users_role as r on u.id = r.user_id WHERE device_id = ?";

            return jdbcTemplate.query(sql, rowMapperUserRoleDevices, id);
        } catch (EmptyResultDataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("Error getting user with device id = " + id + "\n" + e.getMessage());
            }
            return null;
        }
    }

    @Override
    public List<UserRoleDevices> getUserRoleDevicesByDeviceNumber(String deviceNumber) {
        if (log.isDebugEnabled()) {
            log.debug("Try get UserRoleDevices with device number = " + deviceNumber);
        }
        try {
            String sql ="SELECT u.id, u.login, u.password, u.name, u.first_sign_in, u.main_users_id, r.role_id, de.id, de.device_name, " +
                    "de.device_number, de.status, de.hex_name from user as u " +
                    "INNER JOIN users_device AS d ON d.user_id = u.id " +
                    "INNER JOIN device de on d.device_id = de.id " +
                    "INNER JOIN users_role as r on u.id = r.user_id " +
                    "WHERE de.device_number = ?";

            return jdbcTemplate.query(sql, rowMapperUserRoleDevices, deviceNumber);
        } catch (EmptyResultDataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("Error getting user with device number = " + deviceNumber);
            }
            return null;
        }
    }

    @Override
    public boolean existsByUsername(String login) {
        if (log.isDebugEnabled()) {
            log.debug("Try check is User with login exist = " + login);
        }
        String sql = "SELECT id, login, password, name, first_sign_in, main_users_id, role_id FROM user WHERE login = ?";
        List<UserRoleDevices> query = jdbcTemplate.query(sql, rowMapperUserRoleDevices, login);

        return !CollectionUtils.isEmpty(query);
    }
}
