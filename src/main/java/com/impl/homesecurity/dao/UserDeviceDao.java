package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.UserDevice;
import com.impl.homesecurity.domain.UserRoleDevices;
import java.util.Optional;

public interface UserDeviceDao {

    int addUserDevice(UserDevice userDevice);
    int deleteUserDevice(Long userId);
    Optional<UserRoleDevices> getUserRoleDevicesByUserId(long id);
}
