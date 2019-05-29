package com.impl.homesecurity.dao.mapper;

import com.impl.homesecurity.domain.Device;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DeviceRowMapper implements RowMapper<Device> {

    @Override
    public Device mapRow(ResultSet row, int rowNum) throws SQLException {
        Device device = new Device();

        device.setId(row.getLong("id"));
        device.setDeviceNumber(row.getString("device_number"));
        device.setStatus(row.getBoolean("status"));
        device.setName(row.getString("device_name"));
        device.setHexName(row.getString("hex_name"));

        return device;
    }
}
