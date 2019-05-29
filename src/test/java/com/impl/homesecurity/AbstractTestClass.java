package com.impl.homesecurity;

import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.enumeration.Role;
import com.impl.homesecurity.payload.LoginRequest;
import com.impl.homesecurity.service.UserService;
import com.impl.homesecurity.service.impl.MainSecurityService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

/**
 * Created by vyacheslav on 29.12.18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomeSecurityApplication.class)
@AutoConfigureMockMvc
@Transactional
@DirtiesContext
@TestPropertySource(locations="classpath:application-test-res.properties")
public abstract class AbstractTestClass {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private MainSecurityService mainSecurityService;

    private final String superAdminLogin = "380001112266";
    private final String superAdminPassword = "12345";

    /**
     * Создание Юзера с ролью ROLE_SUPERADMIN
     * @return user
     */
    public User createSuperAdmin() {
        String superAdminName = "SuperAdmin";
        User user = new User();
        user.setName(superAdminName);
        user.setLogin(superAdminLogin);
        user.setFirstSignIn(true);
        user.setMainUser(new User());
        user.setRole(Role.ROLE_SUPERADMIN);
        user.setPassword(passwordEncoder.encode(superAdminPassword));
        return user;
    }

    /**
     * Создание Юзера с ролью ROLE_ADMIN
     * @return user
     */
    public User createAdmin() {
        User user = new User();
        user.setName("Admin");
        user.setLogin("380001112255");
        user.setFirstSignIn(true);
        user.setMainUser(new User());
        user.setRole(Role.ROLE_ADMIN);
        user.setPassword(passwordEncoder.encode("12345"));
        return user;
    }

    /**
     * Создание Юзера с ролью ROLE_SUPERUSER
     * @return user
     */
    public User createSuperUser() {
        User user = new User();
        user.setName("SuperUser");
        user.setLogin("380001112244");
        user.setFirstSignIn(true);
        user.setMainUser(new User());
        user.setRole(Role.ROLE_SUPERUSER);
        user.setPassword(passwordEncoder.encode("12345"));
        return user;
    }

    /**
     * Создание Юзера с ролью ROLE_USER
     * @return user
     */
    public User createUser() {
        User user = new User();
        user.setName("User");
        user.setLogin("380001112233");
        user.setFirstSignIn(true);
        user.setMainUser(new User());
        user.setRole(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode("12345"));
        return user;
    }

    /**
     * Получение токена для юзера с ролью ROLE_SUPERADMIN
     * @return Токен
     */
    public String getToken(){
        User user = createSuperAdmin();
        userService.addUser(user);
        user = userService.getUserByLogin(user.getLogin());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(superAdminLogin);
        loginRequest.setPassword(superAdminPassword);

        assertTrue(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));
        return "Bearer " + mainSecurityService.singIn(loginRequest);
    }

    /**
     * Получение токена для существующего юзера.
     * @param user Существующий юзер
     * @return Токен
     */
    public String getToken(User user){
        user = userService.getUserByLogin(user.getLogin());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(user.getLogin());
        loginRequest.setPassword(user.getPassword());

        assertTrue(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));
        return "Bearer " + mainSecurityService.singIn(loginRequest);
    }

}
