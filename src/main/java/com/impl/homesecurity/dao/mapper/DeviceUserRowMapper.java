package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.DeviceUser;
import com.impl.homesecurity.domain.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DeviceUserRowMapper implements RowMapper<DeviceUser> {

    @Override
    public DeviceUser mapRow(ResultSet row, int rowNum) throws SQLException {
        DeviceUser deviceUser = new DeviceUser();
        List<User> users = new ArrayList<>();

        do {
            User user = new User();
            user.setLogin(row.getString("login"));
            user.setPassword(row.getString("password"));
            user.setName(row.getString("name"));
            users.add(user);
        } while (row.next());

        deviceUser.setUsers(users);

        return deviceUser;
    }
}
