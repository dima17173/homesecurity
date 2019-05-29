package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.*;
import com.impl.homesecurity.domain.*;
import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import com.impl.homesecurity.domain.enumeration.Role;
import com.impl.homesecurity.payload.LoginRequest;
import com.impl.homesecurity.payload.SignUpRequest;
import com.impl.homesecurity.security.JwtTokenProvider;
import com.impl.homesecurity.security.UserPrincipal;
import com.impl.homesecurity.util.DecHexConverterUtil;
import com.impl.homesecurity.util.TemporaryPasswordUtil;
import com.impl.homesecurity.web.rest.errors.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Service
public class MainSecurityService {

    private final Logger log = LoggerFactory.getLogger(MainSecurityService.class);
    private final UserDao userDao;
    private final UserRoleDao userRoleDao;
    private final UserDeviceDao userDeviceDao;
    private final DeviceDao deviceDao;
    private final InterconnectionUsersDao interconnectionUsersDao;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final SmsSenderService smsSenderService;
    private PasswordEncoder passwordEncoder;
    private boolean isAdded;

    public MainSecurityService(UserDao userDao, UserRoleDao userRoleDao, UserDeviceDao userDeviceDao,
                               DeviceDao deviceDao, InterconnectionUsersDao interconnectionUsersDao,
                               JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager,
                               SmsSenderService smsSenderService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.userRoleDao = userRoleDao;
        this.userDeviceDao = userDeviceDao;
        this.deviceDao = deviceDao;
        this.interconnectionUsersDao = interconnectionUsersDao;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.smsSenderService = smsSenderService;
        this.passwordEncoder = passwordEncoder;
    }

    //создание аккаунта пользователя
    @Transactional
    public UserRoleDevices signUp(SignUpRequest signUpRequest) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Request to sign up user : {} ", signUpRequest);
        }
        UserRoleDevices user = new UserRoleDevices(signUpRequest.getName(), signUpRequest.getLogin());
        String temporaryPassword = TemporaryPasswordUtil.createTemporaryPassword();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role role;

        user.setPassword(passwordEncoder.encode(temporaryPassword));
        if (log.isInfoEnabled()) {  //todo убрать, если никому уже не нужно
            log.info("Temporary password ------ {} for user : {} ", temporaryPassword, user.getLogin());
        }

        if (principal instanceof UserPrincipal) {

            Collection<? extends GrantedAuthority> authorities = ((UserPrincipal) principal).getAuthorities();
            boolean isValidRole = authorities.stream()
                    .anyMatch(r -> Arrays.asList(Role.ROLE_ADMIN.name(), Role.ROLE_SUPERUSER.name(), Role.ROLE_SUPERADMIN.name(), Role.ROLE_USER.name())
                            .contains(r.getAuthority()));
            String hexName = signUpRequest.getDeviceNumber();

            if (isValidRole) {
                Device device = new Device();
                role = getUserPrincipalRole(authorities);

                if (authorities.stream().anyMatch(r -> r.getAuthority().equals(Role.ROLE_ADMIN.name()))) {
                    isAdded = addDevice(hexName, device);
                    if (!isAdded) {
                        if (log.isWarnEnabled()) {
                            log.warn("Device : {} doesn't exist", device);
                        }
                        throw new AppException(ExceptionCodes.DEVICE_DOES_NOT_EXIST);
                    }
                }

                if(authorities.stream().anyMatch(r -> r.getAuthority().equals(Role.ROLE_SUPERUSER.name()))) {
                    device = deviceDao.getDeviceByNumber(String.valueOf(DecHexConverterUtil.convertHexToDecimal(hexName)));
                    if (Objects.nonNull(device)) isAdded = true;
                }
                user.setMainUser(new User(((UserPrincipal) principal).getId()));
                user.setRole(role);
                user.setAuthorities(Collections.singletonList(role));

                userDao.addUser(user);
                Long userId = userDao.getUserByLogin(user.getLogin()).getId();

                if (isAdded) {
                    addUserDevice(user.getLogin(), device);
                }

                User mainUser = new User();
                mainUser.setId(((UserPrincipal) principal).getId());
                user.setId(userId);

                userRoleDao.addUserRole(new UserRole(userId, role));
                interconnectionUsersDao.addInterconnectionUsers(new InterconnectionUsers(mainUser, user));
            } else {
                if (log.isWarnEnabled()) {
                    log.warn("Role doesn't valid");
                }
                throw new AppException(ExceptionCodes.INVALID_ROLE);
            }
        } else {
            if (log.isErrorEnabled()) {
                log.error("Invalid role for creating. User : {}", user.getLogin());
            }
            throw new AppException(ExceptionCodes.INVALID_ROLE_FOR_CREATED);
        }
        if (userDao.getUserRoleByLogin(user.getLogin()).isPresent()) {
            SmsSender smsSender = new SmsSender(user.getLogin(), temporaryPassword);
            smsSenderService.sentResponse(smsSender);

            return userDao.getUserRoleByLogin(user.getLogin()).get();
        } else {
            if (log.isErrorEnabled()) {
                log.error("Error creating user : {}", user);
            }
            throw new AppException(ExceptionCodes.ERROR_CREATING_USER);
        }
    }

    //создать девайс, если при регистрации пользователя указывался номер девайса
    private boolean addDevice(String hexName, Device device) {
        if (log.isDebugEnabled()) {
            log.debug("Request to add device : {}", hexName);
        }
        if (hexName != null) {
            String deviceNumber = String.valueOf(DecHexConverterUtil.convertHexToDecimal(hexName));
            device.setHexName(hexName);
            device.setStatus(false);
            device.setName("device" + deviceNumber.substring(0, 3));
            device.setDeviceNumber(deviceNumber);

            deviceDao.addDevice(device);
            return true;
        }
        if (log.isWarnEnabled()) {
            log.warn("Hex name of device must not be null");
        }
        return false;
    }

    //запись в связанную таблицу, если у пользователя есть девайс
    private void addUserDevice(String login, Device device) {
        if (log.isDebugEnabled()) {
            log.debug("Request to add user : {} device : {} to users_device table", login, device);
        }
        Long deviceId = deviceDao.getDeviceByNumber(device.getDeviceNumber()).getId();
        Long userId = userDao.getUserByLogin(login).getId();
        device.setId(deviceId);
        userDeviceDao.addUserDevice(new UserDevice(userId, deviceId));
    }

    //определяем роль пользователя
    private Role getUserPrincipalRole(Collection<? extends GrantedAuthority> authorities) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get user role from authorities : {}", authorities);
        }
        Role role;
        if (authorities.stream().anyMatch(r -> r.getAuthority()
                .equals(Role.ROLE_SUPERUSER.name()))) {
            role = Role.ROLE_USER;
        } else if (authorities.stream().anyMatch(r -> r.getAuthority()
                .equals(Role.ROLE_SUPERADMIN.name()))) {
            role = Role.ROLE_ADMIN;
        } else if (authorities.stream().anyMatch(r -> r.getAuthority()
                .equals(Role.ROLE_ADMIN.name()))){
            role = Role.ROLE_SUPERUSER;
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Unable to create user");
            }
            throw new AppException(ExceptionCodes.INVALID_ROLE_FOR_CREATED);
        }
        return role;
    }

    //существует ли пользователь с таким логином
    public boolean isLoginExist(String login) {
        if (log.isDebugEnabled()) {
            log.debug("Try to get user with login {}", login);
        }
        return userDao.existsByUsername(login);
    }

    //авторизация существующего пользователя
    public String singIn(LoginRequest loginRequest) {
        if (log.isDebugEnabled()) {
            log.debug("Try to sign in with login request {}", loginRequest);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }
}
