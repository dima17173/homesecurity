package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.enumeration.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by dima.
 * Creation date 28.11.18.
 */
@Component
public class RoleRowMapper implements RowMapper<Role> {

    @Nullable
    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = Role.valueOf(rs.getString("role"));
        return role;
    }
}
