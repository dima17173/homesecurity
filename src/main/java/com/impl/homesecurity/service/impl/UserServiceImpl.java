package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.InterconnectionUsersDao;
import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.dao.UserDeviceDao;
import com.impl.homesecurity.dao.UserRoleDao;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.UserRole;
import com.impl.homesecurity.domain.UserRoleDevices;
import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import com.impl.homesecurity.payload.ChangePasswordRequest;
import com.impl.homesecurity.service.UserService;
import com.impl.homesecurity.web.rest.errors.AppException;
import com.impl.homesecurity.web.rest.errors.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by dima.
 * Creation date 24.10.18.
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;
    private final UserDeviceDao userDeviceDao;
    private final UserRoleDao userRoleDao;
    private final PasswordEncoder passwordEncoder;
    private final InterconnectionUsersDao interconnectionUsersDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, UserDeviceDao userDeviceDao, UserRoleDao userRoleDao, PasswordEncoder passwordEncoder, InterconnectionUsersDao interconnectionUsersDao) {
        this.userDao = userDao;
        this.userDeviceDao = userDeviceDao;
        this.userRoleDao = userRoleDao;
        this.passwordEncoder = passwordEncoder;
        this.interconnectionUsersDao = interconnectionUsersDao;
    }

    @Override
    public int addUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("Request to add User : {}", user);
        }
        int records = userDao.addUser(user);
        if (records > 0) {
            Long userId = userDao.getUserByLogin(user.getLogin()).getId();
            userRoleDao.addUserRole(new UserRole(userId, user.getRole()));
        }
        return records;
    }

    @Override
    public User getUserById(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get User with id : {}", id);
        }
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByLogin(String login) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get User by login : {}", login);
        }
        return userDao.getUserByLogin(login);
    }

    @Override
    public int updateUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("Request to update User : {}", user);
        }
        return userDao.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        if (log.isDebugEnabled()) {
            log.debug("Request to get all Users");
        }
        return userDao.getAllUsers();
    }

    @Override
    public List<User> getAllUsersByRoleId(long roleId) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get all Users with role id : {}", roleId);
        }
        return userDao.getAllUsersByRoleId(roleId);
    }

    @Override
    public List<User> getAllUsersByMainUser(long mainUserId) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get all Users by Main User id : {}", mainUserId);
        }
        return userDao.getAllUsersByMainUser(mainUserId);
    }

    @Override
    public Optional<UserRoleDevices> getUserRoleDevicesByUserId(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get UserRoleDevices by user id : {}", id);
        }
        return userDeviceDao.getUserRoleDevicesByUserId(id);
    }

    @Override
    public boolean existsByUsername(String login) {
        if (log.isDebugEnabled()) {
            log.debug("Try check is User with login exist = " + login);
        }
        return userDao.existsByUsername(login);
    }

    @Override
    @Transactional
    public int deleteUser(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Request to delete User with id : {}", id);
        }
        User user = userDao.getUserById(id);
        if (user != null){
            if (user.getMainUser() != null) {
                List<User> allUsersByMainUser = userDao.getAllUsersByMainUser(id);
                if (allUsersByMainUser != null) {
                    for (User users : allUsersByMainUser) {
                        users.getMainUser().setId(0L);
                        userDao.updateUser(users);
                    }
                }
                interconnectionUsersDao.deleteAllUsersByMainUser(user.getId());
            }
        } else {
            if (log.isErrorEnabled()) {
                log.error("User with id : {} doesn't exist", id);
                throw new BadRequestException(ExceptionCodes.USER_DOES_NOT_EXIST.getMsg());
            }
        }
        if (userDao.getAllUserRoleDevices() != null) {
            userDeviceDao.deleteUserDevice(id);
        }
        userRoleDao.deleteUserRole(id);

        return userDao.deleteUser(id);
    }

    //изменение пароля
    @Override
    public int changePassword(ChangePasswordRequest changePasswordRequest) {
        if (log.isDebugEnabled()) {
            log.debug("Request to change old password {} for user {}",
                    changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        }
        try {
            User user = userDao.getUserByLogin(changePasswordRequest.getLogin());
            String oldPassword = changePasswordRequest.getOldPassword();
            String userHashPassword = user.getPassword();

            if (passwordEncoder.matches(oldPassword, userHashPassword)) {
                String hashNewPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
                user.setPassword(hashNewPassword);

                return userDao.updateUser(user);
            } else {
                if (log.isInfoEnabled()) {
                    log.info("Old password : {} is not correct", changePasswordRequest.getOldPassword());
                }
                throw new AppException(ExceptionCodes.ERROR_OLD_PASSWORD);
            }
        } catch (EmptyResultDataAccessException e) {
            if (log.isErrorEnabled()) {
                log.error("User with login : {} doesn't exist ", changePasswordRequest.getLogin());
            }
            throw new AppException(ExceptionCodes.USER_DOES_NOT_EXIST);
        }
    }
}
