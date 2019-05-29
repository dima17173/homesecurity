package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.Settings;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dima.
 * Creation date 24.10.18.
 */
@Component
public class SettingsRowMapper implements RowMapper<Settings> {

    @Override
    public Settings mapRow(ResultSet row, int rowNum) throws SQLException {
        Settings settings = new Settings();

        settings.setId(row.getLong("id"));
        settings.setUserId(row.getLong("user_id"));
        settings.setDeviceId(row.getLong("device_id"));
        settings.setValue(row.getString("value"));

        return settings;
    }
}
