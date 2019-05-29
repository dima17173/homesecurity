package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.dao.SettingsDao;
import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.dao.mapper.SettingUserRowMapper;
import com.impl.homesecurity.dao.mapper.SettingsRowMapper;
import com.impl.homesecurity.domain.SettingUser;
import com.impl.homesecurity.domain.Settings;
import com.impl.homesecurity.web.rest.errors.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by dima.
 * Creation date 23.10.18.
 */
@Repository
public class SettingsDaoImp implements SettingsDao {

    private final Logger log = LoggerFactory.getLogger(SettingsDaoImp.class);
    private final JdbcTemplate jdbcTemplate;
    private final SettingsRowMapper settingsRowMapper;
    private final SettingUserRowMapper settingUserRowMapper;
    private final UserDao userDao;
    private final DeviceDao deviceDao;

    public SettingsDaoImp(@Qualifier("mysqlJdbcTemplateApp") JdbcTemplate jdbcTemplate,
                          SettingsRowMapper settingsRowMapper, UserDao userDao, DeviceDao deviceDao, SettingUserRowMapper settingUserRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.settingsRowMapper = settingsRowMapper;
        this.userDao = userDao;
        this.deviceDao = deviceDao;
        this.settingUserRowMapper = settingUserRowMapper;
    }

    @Override
    public int addSettings(Settings settings) {
        if (log.isDebugEnabled()) {
            log.debug("Try create settings");
        }
        try {
            String sql = "INSERT INTO settings (user_id, device_id, `value`) " +
                    "VALUES (?, ?, ?)";

            return jdbcTemplate.update(sql, settings.getUserId(), settings.getDeviceId(),
                    settings.getValue());
        } catch (DuplicateKeyException e) {
            if (log.isErrorEnabled()) {
                log.error("Error adding settings, settings =  " + settings + "\n" + e.getMessage());
            }
            return 0;
        }
    }

    @Override
    public int updateSettings(Settings settings) {
        if (log.isDebugEnabled()) {
            log.debug("try update settings");
        }
        String sql = "UPDATE settings SET user_id = ?, device_id = ?, `value` = ? WHERE id = ?";

        return jdbcTemplate.update(sql, settings.getUserId(), settings.getDeviceId(),
                settings.getValue(), settings.getId());
    }

    @Override
    public int deleteSettings(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Try delete settings with id = " + id);
        }
        String sql = "DELETE FROM settings WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Settings> getAllSettings() {
        if (log.isDebugEnabled()) {
            log.debug("Try get all settings");
        }
        String sql = "SELECT id, user_id, device_id, `value` FROM settings";

        return jdbcTemplate.query(sql, settingsRowMapper);
    }


    @Override
    public Settings getSettingsById(long settingsId) {
        if (log.isDebugEnabled()) {
            log.debug("try get settings with settings id = " + settingsId);
        }
        try {
            String sql = "SELECT id, user_id, device_id, `value` FROM settings WHERE id = ?";

            return jdbcTemplate.queryForObject(sql, settingsRowMapper, settingsId);
        } catch (EmptyResultDataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("Error getting settings " + "\n" + e.getMessage());
            }
            return null;
        }
    }

    @Override
    public Settings getSettingsByUserId(long userId) {
        if (log.isDebugEnabled()) {
            log.debug("try get settings with user id = " + userId);
        }
        String sql = "SELECT id, user_id, device_id, `value` FROM settings WHERE user_id = ?";

        return jdbcTemplate.queryForObject(sql, settingsRowMapper, userId);

    }

    @Override
    public List<SettingUser> getSettingAndUserNameByDeviceId(long deviceId) {
        if (log.isDebugEnabled()) {
            log.debug("try get settings with device id = " + deviceId);
        }
        String sql = "SELECT s.id, u.id AS user_id, u.name, s.value FROM settings s INNER JOIN user u  ON s.user_id=u.id WHERE device_id =?";
        return jdbcTemplate.query(sql, settingUserRowMapper, deviceId);
    }
}
