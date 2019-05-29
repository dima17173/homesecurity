package com.impl.homesecurity.service;

import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.security.UserPrincipal;
import java.util.List;

public interface DeviceService {

    List<Device> getAllDevices();
    int addDevice(Device device);
    boolean updateDevices(UserPrincipal user);
    Device getDeviceById(long id);
    Device getDeviceByNumber(String deviceNumber);
}
