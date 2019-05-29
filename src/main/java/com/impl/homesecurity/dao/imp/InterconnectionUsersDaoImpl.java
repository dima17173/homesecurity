package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.InterconnectionUsersDao;
import com.impl.homesecurity.dao.mapper.InterconnectionUsersRowMapper;
import com.impl.homesecurity.dao.mapper.MainUserRowMapper;
import com.impl.homesecurity.domain.InterconnectionUsers;
import com.impl.homesecurity.domain.MainUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class InterconnectionUsersDaoImpl implements InterconnectionUsersDao {

    private final Logger log = LoggerFactory.getLogger(InterconnectionUsersDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final InterconnectionUsersRowMapper interconnectionUsersRowMapper;
    private final MainUserRowMapper mainUserRowMapper;

    @Autowired
    public InterconnectionUsersDaoImpl(@Qualifier("mysqlJdbcTemplateApp") JdbcTemplate jdbcTemplate, InterconnectionUsersRowMapper interconnectionUsersRowMapper, MainUserRowMapper mainUserRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.interconnectionUsersRowMapper = interconnectionUsersRowMapper;
        this.mainUserRowMapper = mainUserRowMapper;
    }

    @Override
    public List<MainUser> getAllUsersDevicesByMainUserId(long mainUserId) {
        if (log.isDebugEnabled()) {
            log.debug("try get users by main user = " + mainUserId);
        }
        String sql ="SELECT u.id, u.login, u.name, d.hex_name FROM users_device AS ud\n" +
                "    INNER JOIN user AS u ON ud.user_id = u.id\n" +
                "    INNER JOIN device AS d ON ud.device_id = d.id\n" +
                "    WHERE u.main_users_id = ?";
        return jdbcTemplate.query(sql, mainUserRowMapper, mainUserId);
    }

    @Override
    public InterconnectionUsers getInterconnectionUserByMainUser(long mainUserId) {
        if (log.isDebugEnabled()) {
            log.debug("try get user by main user = " + mainUserId);
        }
        String sql = "SELECT id, user_id FROM interconnection_users WHERE main_user_id = ?";
        return jdbcTemplate.queryForObject(sql, interconnectionUsersRowMapper, mainUserId);
    }

    @Override
    public int addInterconnectionUsers(InterconnectionUsers interconnectionUsers) {
        if (log.isDebugEnabled()) {
            log.debug("try adding new interconnection users to DB");
        }
        String sql = "INSERT INTO interconnection_users (main_user_id, user_id) VALUES (?,?)";
        return jdbcTemplate.update(sql, interconnectionUsers.getMainUser().getId(),
                interconnectionUsers.getUser().getId());
    }

    @Override
    public int deleteUserByMainUser(long mainUserId, long userId) {
        if (log.isDebugEnabled()) {
            log.debug("try delete user by number main user = " + mainUserId + ", user id = " + userId);
        }
        String sql = "DELETE FROM interconnection_users WHERE main_user_id = ?  AND user_id = ?";
        return jdbcTemplate.update(sql, mainUserId, userId);
    }

    @Override
    public int deleteAllUsersByMainUser(long userId) {
        if (log.isDebugEnabled()) {
            log.debug("try delete users by number main user = " + userId);
        }
        String sql = "DELETE FROM interconnection_users WHERE user_id = ? OR main_user_id = ?";
        return jdbcTemplate.update(sql, userId, userId);
    }

    @Override
    public int deleteUserById(Long userId) {
        if (log.isDebugEnabled()) {
            log.debug("try delete user by id = " + userId);
        }
        String sql = "DELETE FROM interconnection_users WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }
}
