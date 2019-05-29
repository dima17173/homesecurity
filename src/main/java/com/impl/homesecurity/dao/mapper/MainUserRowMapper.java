package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.MainUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dima.
 * Creation date 28.11.18.
 */
@Component
public class MainUserRowMapper implements RowMapper<MainUser> {

    @Override
    public MainUser mapRow(ResultSet row, int rowNum) throws SQLException {
        MainUser user = new MainUser();

        user.setId(row.getLong("id"));
        user.setLogin(row.getString("login"));
        user.setName(row.getString("name"));
        user.setDeviceName(row.getString("hex_name"));

        return user;
    }
}
