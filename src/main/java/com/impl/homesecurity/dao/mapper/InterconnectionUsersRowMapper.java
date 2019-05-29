package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.InterconnectionUsers;
import com.impl.homesecurity.domain.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class InterconnectionUsersRowMapper implements RowMapper<InterconnectionUsers> {
    @Override
    public InterconnectionUsers mapRow(ResultSet row, int i) throws SQLException {
        InterconnectionUsers interconnectionUsers = new InterconnectionUsers();

        User user = new User();
        user.setId(row.getLong("id"));
        user.setLogin(row.getString("login"));
        user.setName(row.getString("name"));

        interconnectionUsers.setUser(user);

        return interconnectionUsers;
    }
}
