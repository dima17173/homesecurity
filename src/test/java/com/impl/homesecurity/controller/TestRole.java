package com.impl.homesecurity.controller;

import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.web.rest.RoleResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dima.
 * Creation date 11.12.18.
 */
public class TestRole extends AbstractTestClass {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoleResource roleResource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(roleResource).build();
    }

    @Test
    public void getAllRoles() throws Exception {
        mockMvc.perform(get("/api/role?sort=id,desc"))
                .andExpect(status().isOk());
    }
}
