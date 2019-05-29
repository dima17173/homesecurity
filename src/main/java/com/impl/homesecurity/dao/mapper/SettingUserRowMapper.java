package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.SettingUser;
import com.impl.homesecurity.domain.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SettingUserRowMapper implements RowMapper<SettingUser> {
    @Override
    public SettingUser mapRow(ResultSet row, int rowNum) throws SQLException {
        SettingUser settingUser = new SettingUser();
        User user = new User();


        settingUser.setId(row.getLong("id"));
        user.setId(row.getLong("user_id"));
        user.setName(row.getString("name"));
        settingUser.setUserId(user.getId());
        settingUser.setUserName(user.getName());
        settingUser.setValue(row.getString("value"));


        return settingUser;
    }
}
