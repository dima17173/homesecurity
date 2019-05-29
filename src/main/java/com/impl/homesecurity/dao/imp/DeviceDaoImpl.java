package com.impl.homesecurity.dao.imp;

import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.dao.mapper.DeviceRowMapper;
import com.impl.homesecurity.dao.mapper.DeviceUserRowMapper;
import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.domain.DeviceUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DeviceDaoImpl implements DeviceDao  {

    private final Logger log = LoggerFactory.getLogger(DeviceDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final DeviceRowMapper deviceRowMapper;
    private final DeviceUserRowMapper deviceUserRowMapper;

    @Autowired
    public DeviceDaoImpl(@Qualifier("mysqlJdbcTemplateApp") JdbcTemplate jdbcTemplate, DeviceRowMapper deviceRowMapper, DeviceUserRowMapper deviceUserRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.deviceRowMapper = deviceRowMapper;
        this.deviceUserRowMapper = deviceUserRowMapper;
    }

    @Override
    public List<Device> getAllDevices() {
        if (log.isDebugEnabled()) {
            log.debug("Try get all devices");
        }
        String sql = "SELECT id, device_number, status, device_name, hex_name FROM device";
        return jdbcTemplate.query(sql, deviceRowMapper);
    }

    @Override
    public Device getDeviceByNumber(String deviceNumber) {
        if (log.isDebugEnabled()) {
            log.debug("try get device with number = " + deviceNumber);
        }
        String sql = "SELECT id, device_number, status, device_name, hex_name FROM device WHERE device_number = ?";
        return jdbcTemplate.queryForObject(sql, deviceRowMapper, deviceNumber);
    }

    @Override
    public Device getDeviceById(long id) {
        if (log.isDebugEnabled()) {
            log.debug("try get device with id = " + id);
        }
        String sql = "SELECT id, device_number, status, device_name, hex_name FROM device WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, deviceRowMapper, id);
    }

    @Override
    public int addDevice(Device device) {
        if (log.isDebugEnabled()) {
            log.debug("try adding new device to DB");
        }
        try {
            String sql = "INSERT INTO device (device_number, status, device_name, hex_name) " +
                    "VALUES (?, ?, ?, ?)";
            return jdbcTemplate.update(sql, device.getDeviceNumber(), device.isStatus(), device.getName(),
                    device.getHexName());

        } catch (DuplicateKeyException e) {
            if (log.isErrorEnabled())
            log.error("Error adding device, device =  " + device + "\n" + e.getMessage());
            return 0;
        }
    }

    @Override
    public int updateDevice(Device device) {
        if (log.isDebugEnabled()) {
            log.debug("try update device");
        }
        String sql = "UPDATE device SET device_number = ?, status = ?, device_name = ?, hex_name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, device.getDeviceNumber(), device.isStatus(), device.getName(), device.getHexName(), device.getId());
    }

    @Override
    public int[] updateDeviceAndInverseStatus(List<Device> devices) {
        if (log.isDebugEnabled()) {
            log.debug("try update devices list");
        }
        String sql = "UPDATE device SET device_number = ?, status = !status, device_name = ?, hex_name = ? WHERE id = ?";
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Device device = devices.get(i);
                ps.setString(1, device.getDeviceNumber());
                ps.setString(2, device.getName());
                ps.setString(3, device.getHexName());
                ps.setLong(4, device.getId());
            }

            @Override
            public int getBatchSize() {
                return devices.size();
            }
        });
    }

    @Override
    public int deleteDeviceByNumber(long deviceNumber) {
        if (log.isDebugEnabled()) {
            log.debug("try delete device with number = " + deviceNumber);
        }
        String sql = "DELETE FROM device WHERE device_number = ?";
        return jdbcTemplate.update(sql, deviceNumber);
    }

    @Override
    public int deleteDeviceById(long id) {
        if (log.isDebugEnabled()) {
            log.debug("try delete device with id = " + id);
        }
        String sql = "DELETE FROM device WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<DeviceUser> getAllUsersDevicesByUserId(long userId) {
        if (log.isDebugEnabled()) {
            log.debug("try get list of devices with user id = " + userId);
        }
        String sql = "SELECT d.id, d.device_number, d.status, d.device_name,d.hex_name," +
                "u.id, u.login, u.password, u.name, u.first_sign_in FROM device AS d\n" +
                "INNER JOIN users_device AS de ON d.id = de.device_id\n" +
                "INNER JOIN user AS u ON u.id = de.user_id WHERE u.id = ?";
        return jdbcTemplate.query(sql, deviceUserRowMapper, userId);
    }

    @Override
    public List<Device> getAllDevicesByUserId(long userId) {
        if (log.isDebugEnabled()) {
            log.debug("try get list of devices with user id = " + userId);
        }
        String sql = "SELECT d.id, d.device_number, d.status, d.device_name,d.hex_name " +
                "FROM device AS d\n" +
                "INNER JOIN users_device AS de ON d.id = de.device_id\n" +
                "INNER JOIN user AS u ON u.id = de.user_id WHERE u.id = ?";
        return jdbcTemplate.query(sql, deviceRowMapper, userId);
    }
}
