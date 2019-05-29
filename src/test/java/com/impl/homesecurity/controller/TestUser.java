package com.impl.homesecurity.controller;

import com.google.gson.Gson;
import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.enumeration.Role;
import com.impl.homesecurity.service.UserService;
import com.impl.homesecurity.web.rest.UserResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dima.
 * Creation date 31.10.18.
 */
public class TestUser extends AbstractTestClass {

    @Autowired
    Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private UserResource userResource;
    @Autowired
    private UserService userService;

    private User user;
    private String token;

    public User createUser() {
        User user = new User();
        user.setName("Jack");
        user.setLogin("380545445451");
        user.setFirstSignIn(true);
        user.setMainUser(new User());
        user.setRole(Role.ROLE_USER);
        return user;
    }

    @Before
    public  void setUp() {
        token = getToken();
        user = createUser();
        userService.addUser(user);
        user = userService.getUserByLogin(user.getLogin());
    }

    @After
    public void closed(){
        if (user != null) {
            userService.deleteUser(user.getId());
        }

    }

    @Test
    public void getUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/user/?id={id}", user.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(user)))
                .andReturn();

        String str = result.getResponse().getContentAsString();
        assertTrue(str.contains("\"id\":" + user.getId()));
    }

    @Test()
    public void deleteUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/user?id={id}", user.getId())
                .header("Authorization", token))
                .andExpect(status().isOk());
        try {
            userService.getUserById(user.getId());
        } catch (EmptyResultDataAccessException e){
            user = null;
        }
    }

    @Test
    public void updateUser() throws Exception {
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/api/user")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());
    }

    @Test
    public void addUser() throws Exception {
        mockMvc.perform(post("/api/user")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Maikl\",\"login\":\"380545445452\"," +
                        "\"firstSignIn\":true,\"mainUser\":{\"id\":null},\"role\":\"ROLE_ADMIN\"}"))
                .andDo(print())
                .andExpect(status().isOk());

            User userTest = userService.getUserByLogin("380545445452");

        assertEquals("380545445452", userTest.getLogin());
        userService.deleteUser(userTest.getId());
    }
}
