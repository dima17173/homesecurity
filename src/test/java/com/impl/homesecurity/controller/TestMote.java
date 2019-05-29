package com.impl.homesecurity.controller;

import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.domain.Mote;
import com.impl.homesecurity.service.MoteService;
import com.impl.homesecurity.web.rest.MoteResource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dima.
 * Creation date 08.11.18.
 */
public class TestMote extends AbstractTestClass {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MoteResource moteResource;

    private Mote mote;
    private String token;

    public static Mote createMote() {
        Mote mote = new Mote();
        mote.setEui("104432976855679854");
        return mote;
    }

    @Before
    public void setUp() {
        token = getToken();
        mote = createMote();
    }

    @Test
    public void getAllMotes() throws Exception {
        mockMvc.perform(get("/api/freeDevice?sort=id,desc").header("Authorization", token))
                .andExpect(status().isOk());
    }
}
