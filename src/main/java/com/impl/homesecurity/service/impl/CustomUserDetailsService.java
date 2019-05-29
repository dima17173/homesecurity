package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.domain.UserRoleDevices;
import com.impl.homesecurity.security.UserPrincipal;
import com.impl.homesecurity.web.rest.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UserDao userRepository;

    @Autowired
    public CustomUserDetailsService(UserDao userRepository) {
        this.userRepository = userRepository;
    }

    //загрузка пользователя по логину
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("Try load user by login {} ", login);
        }
        UserRoleDevices user = userRepository.getUserRoleByLogin(login).orElseThrow(() ->
                    new UsernameNotFoundException("User not found with login : " + login)
        );
        return UserPrincipal.create(user);
    }

    //загрузка пользователя по id
    public UserDetails loadUserById(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Try load user by id {} ", id);
        }
        UserRoleDevices user = userRepository.getUserByIdWithRole(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id)
        );
        return UserPrincipal.create(user);
    }
}
