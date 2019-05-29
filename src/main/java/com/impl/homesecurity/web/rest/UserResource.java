package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.UserRoleDevices;
import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import com.impl.homesecurity.payload.ApiExceptionResponse;
import com.impl.homesecurity.payload.ChangePasswordRequest;
import com.impl.homesecurity.security.CurrentUser;
import com.impl.homesecurity.security.UserPrincipal;
import com.impl.homesecurity.service.UserService;
import com.impl.homesecurity.util.HeaderUtil;
import com.impl.homesecurity.util.PhoneValidatorUtil;
import com.impl.homesecurity.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by dima.
 * Creation date 24.10.18.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    private static final String ENTITY_NAME = "user";
    private final UserService userService;

    @PostMapping("/user")
    @Timed
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERUSER') or hasRole('ROLE_SUPERADMIN')")
    public ResponseEntity createUser(@RequestBody User user) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to save User : {}", user);
        }
        if (user.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", ENTITY_NAME, "id exists");
        } else if (userService.addUser(user) > 0) {
            long userId = userService.getUserByLogin(user.getLogin()).getId();

            return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
                    String.valueOf(userId))).build();
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/user")
    @Timed
    public ResponseEntity updateUser(@RequestBody User user) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to update User : {}", user);
        }
        if (user.getId() == null) {
            return createUser(user);
        } else if (userService.updateUser(user) > 0) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
                    user.getId().toString())).build();
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/user")
    @Timed
    public ResponseEntity<User> getUser(@RequestParam(name = "id") long id) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get User : {}", id);
        }
        User user = userService.getUserById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(user));
    }

    @GetMapping("/users")
    @Timed
    public List<User> getAllUsers() {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get all Users");
        }
        return userService.getAllUsers();
    }

    @GetMapping("/users/role")
    @Timed
    public List<User> getAllUsersByRoleId(@RequestParam(name = "id") long roleId) {
        return userService.getAllUsersByRoleId(roleId);
    }

    @GetMapping("/user/role/device")
    @Timed
    public Optional<UserRoleDevices> getUserRoleDevicesByUserId(@RequestParam(name = "id") Long mainUserId){
        if (log.isDebugEnabled()) {
            log.debug("REST request to get User : {}", mainUserId);
        }
        return userService.getUserRoleDevicesByUserId(mainUserId);
    }

    @GetMapping("/getAllUsersByMainUser")
    @Timed
    public List<User> getAllUsersByMainUser(@RequestParam(name = "id") long mainUserId) {
        return userService.getAllUsersByMainUser(mainUserId);
    }

    @DeleteMapping("/user")
    @Timed
    public ResponseEntity deleteProfile(@RequestParam(name = "id") Long id) {
        if (log.isInfoEnabled()) {
            log.info("REST request to delete User : {}", id);
        }
        if (userService.deleteUser(id) > 0) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME,
                    id.toString())).build();
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/user/changePass")
    @Timed
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest passwordRequest, @CurrentUser UserPrincipal userPrincipal) {
        if (log.isInfoEnabled()) {
            log.info("REST request to update User password : {}", passwordRequest.getLogin());
        }
        if (!PhoneValidatorUtil.validatePhoneNumber(passwordRequest.getLogin())) {
            return new ResponseEntity<>(new ApiExceptionResponse(ExceptionCodes.INVALID_PHONE), HttpStatus.BAD_REQUEST);
        }
        if (!userPrincipal.getUsername().equals(passwordRequest.getLogin())) {
            return new ResponseEntity<>(new ApiExceptionResponse(ExceptionCodes.INVALID_LOGIN), HttpStatus.BAD_REQUEST);
        }
        if (userService.changePassword(passwordRequest) > 0) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
                    passwordRequest.toString())).build();
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
