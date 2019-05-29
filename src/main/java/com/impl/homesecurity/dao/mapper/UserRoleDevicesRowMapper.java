package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.domain.UserRoleDevices;
import com.impl.homesecurity.domain.enumeration.Role;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRoleDevicesRowMapper implements RowMapper<UserRoleDevices> {
    @Override
    public UserRoleDevices mapRow(ResultSet row, int rowNum) throws SQLException {
        UserRoleDevices userRoleDevices = new UserRoleDevices();

        userRoleDevices.setId(row.getLong("id"));
        userRoleDevices.setLogin(row.getString("login"));
        userRoleDevices.setName(row.getString("name"));
        userRoleDevices.setPassword(row.getString("password"));
        userRoleDevices.setFirstSignIn(row.getBoolean("first_sign_in"));

        ResultSetMetaData metaData = row.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i < columnCount + 1; i++) {
            String columnName = metaData.getColumnName(i);
            if (columnName.equals("role_id")) {
                List<Role> roles = new ArrayList<>();
                roles.add(Role.getRoleByNumber(row.getInt("role_id")));
                userRoleDevices.setAuthorities(roles);
            }
            else if (columnName.equals("device_number")) {
                List<Device> devices = new ArrayList<>();
                do {
                    Device device = new Device();
                    device.setId(row.getLong("device_id"));
                    device.setDeviceNumber(row.getString("device_number"));
                    device.setName(row.getString("device_name"));
                    device.setStatus(row.getBoolean("status"));
                    device.setHexName(row.getString("hex_name"));
                    devices.add(device);
                } while (row.next());
                userRoleDevices.setDevices(devices);
            }
        }
        return userRoleDevices;
    }
}
