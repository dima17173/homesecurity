package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.enumeration.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet row, int rowNum) throws SQLException {
        User user = new User();

        user.setId(row.getLong("id"));
        user.setLogin(row.getString("login"));
        user.setPassword(row.getString("password"));
        user.setName(row.getString("name"));
        user.setFirstSignIn(row.getBoolean("first_sign_in"));

        User mainUser = new User();
        mainUser.setId(row.getLong("main_users_id"));
        user.setMainUser(mainUser);
        user.setRole(Role.getRoleByNumber(row.getInt("role_id")));

        return user;
    }
}
