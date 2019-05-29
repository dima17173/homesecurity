package com.impl.homesecurity.controller;

import com.google.gson.Gson;
import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.payload.LoginRequest;
import com.impl.homesecurity.service.UserService;
import com.impl.homesecurity.service.impl.MainSecurityService;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAuthentication extends AbstractTestClass {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MainSecurityService mainSecurityService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    Gson gson;
    private User user;
    private User superUser;
    private User admin;
    private User superAdmin;

    @After
    public void closed() {
        if (user != null) {
            userService.deleteUser(user.getId());
        }
        if (superUser != null) {
            userService.deleteUser(superUser.getId());
        }
        if (admin != null) {
            userService.deleteUser(admin.getId());
        }
        if (superAdmin != null) {
            userService.deleteUser(superAdmin.getId());
        }
    }

    /**
     * Тест без авторизации
     *
     * @throws Exception
     */
    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/device")).andExpect(status().isUnauthorized());
        //todo добавить тестируемые ресты
    }

    /**
     * Тест signIn без авторизации
     *
     * @throws Exception
     */
    @Test
    public void signInForUnauthenticatedUsers() throws Exception {
        int statusSignIn = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signIn")).andReturn().getResponse().getStatus();
        assertNotEquals(statusSignIn,401);

        int statusForgotPassword = mvc.perform(MockMvcRequestBuilders.post("/api/auth/forgotPassword")).andReturn().getResponse().getStatus();
        assertNotEquals(statusForgotPassword,401);

        int statusSignUp = mvc.perform(MockMvcRequestBuilders.post("/api/auth/signUp")).andReturn().getResponse().getStatus();
        assertEquals(statusSignUp,401);
    }

    /**
     * Тест с невалидным токеном
     *
     * @throws Exception
     */
    @Test
    public void badAuthToken() throws Exception {

        String token = "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a";

        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/api/device").header("Authorization", token)).andExpect(status().isUnauthorized());
        //todo добавить тестируемые ресты
    }

    /**
     * Тест с авторизацией и ролью USER
     *
     * @throws Exception
     */
    @Test
    public void generateAuthTokenRoleUser() throws Exception {

        user = createUser();
        userService.addUser(user);
        user = userService.getUserByLogin(user.getLogin());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("380001112233");
        loginRequest.setPassword("12345");

        assertTrue(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));
        String token = "Bearer " + mainSecurityService.singIn(loginRequest);

//        User superUser = createSuperUser();

        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/api/device").header("Authorization", token)).andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.delete("/api/user/?id={id}", user.getId()).header("Authorization", token)).andExpect(status().isForbidden());
        mvc.perform(MockMvcRequestBuilders.post("/api/user").header("Authorization", token)
//                .contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf())
//                .accept(MediaType.APPLICATION_JSON)
//                .content(gson.toJson(superUser))
        ).andExpect(status().isForbidden());
        //todo добавить тестируемые ресты
    }

    /**
     * Тест с авторизацией и ролью SUPERUSER
     *
     * @throws Exception
     */
    @Test
    public void generateAuthTokenRoleSuperUser() throws Exception {

        superUser = createSuperUser();
        userService.addUser(superUser);
        superUser = userService.getUserByLogin(superUser.getLogin());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("380001112244");
        loginRequest.setPassword("12345");

        assertTrue(passwordEncoder.matches(loginRequest.getPassword(), superUser.getPassword()));
        String token = "Bearer " + mainSecurityService.singIn(loginRequest);

        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/api/device").header("Authorization", token)).andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/api/user/?id={id}", superUser.getId()).header("Authorization", token)).andExpect(status().isOk());
        //todo добавить тестируемые ресты
    }

    /**
     * Тест с авторизацией и ролью ROLE_ADMIN
     *
     * @throws Exception
     */
    @Test
    public void generateAuthTokenRoleAdmin() throws Exception {

        admin = createAdmin();
        userService.addUser(admin);
        admin = userService.getUserByLogin(admin.getLogin());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("380001112255");
        loginRequest.setPassword("12345");

        assertTrue(passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword()));
        String token = "Bearer " + mainSecurityService.singIn(loginRequest);

        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/api/device").header("Authorization", token)).andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/api/user/?id={id}", admin.getId()).header("Authorization", token)).andExpect(status().isOk());
        //todo добавить тестируемые ресты
    }

    /**
     * Тест с авторизацией и ролью SUPERUSER
     *
     * @throws Exception
     */
    @Test
    public void generateAuthTokenRoleSuperAdmin() throws Exception {

        superAdmin = createSuperAdmin();
        userService.addUser(superAdmin);
        superAdmin = userService.getUserByLogin(superAdmin.getLogin());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("380001112266");
        loginRequest.setPassword("12345");

        assertTrue(passwordEncoder.matches(loginRequest.getPassword(), superAdmin.getPassword()));
        String token = "Bearer " + mainSecurityService.singIn(loginRequest);

        assertNotNull(token);
        mvc.perform(MockMvcRequestBuilders.get("/api/device").header("Authorization", token)).andExpect(status().isOk());
        mvc.perform(MockMvcRequestBuilders.get("/api/user/?id={id}", superAdmin.getId()).header("Authorization", token)).andExpect(status().isOk());
        //todo добавить тестируемые ресты
    }
}
