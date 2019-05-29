package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.UserRoleDevices;
import java.util.List;
import java.util.Optional;

public interface UserDao {

    int addUser(User user);
    User getUserById(long id);
    User getUserByLogin(String login);
    Optional<UserRoleDevices> getUserByIdWithRole(long id);
    int updateUser(User user);
    int deleteUser(long id);
    Optional<UserRoleDevices> getUserRoleByLogin(String login);
    List<User> getAllUsers();
    List<User> getAllUsersByRoleId(long roleId);
    List<UserRoleDevices> getAllUserRoleDevices();
    List<User> getUsersByDeviceId(long id);
    List<UserRoleDevices> getUserRoleDevicesByDeviceId(long id);
    List<UserRoleDevices> getUserRoleDevicesByDeviceNumber(String deviceNumber);
    List<User> getAllUsersByMainUser(long mainUserId);
    boolean existsByUsername(String login);
}
