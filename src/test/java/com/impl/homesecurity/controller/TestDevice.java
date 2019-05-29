package com.impl.homesecurity.controller;

import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.web.rest.DeviceResource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.Filter;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dima.
 * Creation date 08.11.18.
 */
public class TestDevice extends AbstractTestClass {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DeviceResource deviceResource;

    private Device device;
    private String token;

    public static Device createDevice() {
        Device device = new Device();
        device.setDeviceNumber("7");
        device.setStatus(true);
        device.setName("device17");
        device.setHexName("00");
        return device;
    }

    @Before
    public void setUp() {
        token = getToken();
        device = createDevice();
    }

    @Test
    public void addDevice() throws Exception {
        mockMvc.perform(post("/api/device").content(
                "{" +
                        "\"device_number\":\"7\"," +
                        "\"status\":true," +
                        "\"device_name\":\"device17\"," +
                        "\"hex_name\": \"00\"" +
                        "}").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllDevices() throws Exception {
        mockMvc.perform(get("/api/device?sort=id,desc").header("Authorization", token)).andExpect(status().isOk());
    }
}
