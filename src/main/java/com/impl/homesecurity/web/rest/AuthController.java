package com.impl.homesecurity.web.rest;

import com.impl.homesecurity.domain.SmsSender;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.UserRoleDevices;
import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import com.impl.homesecurity.payload.*;
import com.impl.homesecurity.service.UserService;
import com.impl.homesecurity.service.impl.MainSecurityService;
import com.impl.homesecurity.service.impl.SmsSenderService;
import com.impl.homesecurity.util.HeaderUtil;
import com.impl.homesecurity.util.PhoneValidatorUtil;
import com.impl.homesecurity.util.TemporaryPasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final MainSecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    private final SmsSenderService smsSenderService;
    private final UserService userService;

    @Autowired
    public AuthController(MainSecurityService securityService, PasswordEncoder passwordEncoder,
                          SmsSenderService smsSenderService, UserService userService) {
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
        this.smsSenderService = smsSenderService;
        this.userService = userService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        if (log.isDebugEnabled()) {
            log.debug("Try to sign in with user login = " + loginRequest.getLogin());
        }
        return ResponseEntity.ok(new JwtAuthenticationResponse(securityService.singIn(loginRequest)));
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Try to register user with login = " + signUpRequest.getLogin());
        }
        if (!PhoneValidatorUtil.validatePhoneNumber(signUpRequest.getLogin())) {
            return new ResponseEntity<>(new ApiExceptionResponse(ExceptionCodes.INVALID_PHONE), HttpStatus.BAD_REQUEST);
        }
        if(securityService.isLoginExist(signUpRequest.getLogin())) {
            return new ResponseEntity<>(new ApiExceptionResponse(ExceptionCodes.DUPLICATE_LOGIN),
                    HttpStatus.BAD_REQUEST);
        }
        UserRoleDevices result = securityService.signUp(signUpRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getName()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(Boolean.TRUE, "User registered successfully"));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestParam(name = "login") String login) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Try to change password");
        }
        if (userService.existsByUsername(login)) {
            User user = userService.getUserByLogin(login);
            String tempPassword = TemporaryPasswordUtil.createTemporaryPassword();
            smsSenderService.sentResponse(new SmsSender(login, tempPassword));
            if (log.isInfoEnabled()) {
                log.info(tempPassword);
            }

            String newPassword = passwordEncoder.encode(tempPassword);
            user.setPassword(newPassword);
            userService.updateUser(user);

            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Password", login)).build();
        } else {
            return new ResponseEntity<>(new ApiExceptionResponse(ExceptionCodes.USER_DOES_NOT_EXIST),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
