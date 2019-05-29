package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.domain.DeviceUser;
import java.util.List;

public interface DeviceDao {

    List<Device> getAllDevices();
    Device getDeviceByNumber(String deviceNumber);
    Device getDeviceById(long id);
    int addDevice(Device device);
    int updateDevice(Device device);
    int[] updateDeviceAndInverseStatus(List<Device> devices);
    int deleteDeviceByNumber(long deviceNumber);
    int deleteDeviceById(long id);
    List<DeviceUser> getAllUsersDevicesByUserId(long userId);
    List<Device> getAllDevicesByUserId(long userId);
}
