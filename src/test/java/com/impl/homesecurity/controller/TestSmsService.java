package com.impl.homesecurity.controller;

import com.google.gson.Gson;
import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.domain.SmsSender;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dima.
 * Creation date 27.11.18.
 */
public class TestSmsService extends AbstractTestClass {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @Before
    public void setUp() {
        token = getToken();
    }

    @Test
    @Ignore
    public void sentResponse() throws Exception {

        SmsSender smsSender = new SmsSender();
        smsSender.setPhone("380957414414");
        smsSender.setMessage("235436");

        Gson gson = new Gson();
        String json = gson.toJson(smsSender);

        mockMvc.perform(post("/api/sms/sent")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    @Ignore
    public void getMessages() throws Exception {
        mockMvc.perform(put("/api/sms/device")
                .header("Authorization", token))
                .andExpect(status().isOk());
    }
}