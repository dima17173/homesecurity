package com.impl.homesecurity.service;

import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.UserRoleDevices;
import com.impl.homesecurity.payload.ChangePasswordRequest;
import java.util.List;
import java.util.Optional;

/**
 * Created by dima.
 * Creation date 24.10.18.
 */
public interface UserService {

    int addUser(User user);
    User getUserById(long id);
    User getUserByLogin(String login);
    int updateUser(User user);
    int deleteUser(Long id);
    int changePassword(ChangePasswordRequest changePasswordRequest);
    List<User> getAllUsers();
    List<User> getAllUsersByRoleId(long roleId);
    List<User> getAllUsersByMainUser(long mainUserId);
    Optional<UserRoleDevices> getUserRoleDevicesByUserId(long id);
    boolean existsByUsername(String login);
}
