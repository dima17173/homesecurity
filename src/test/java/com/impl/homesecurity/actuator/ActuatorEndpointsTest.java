package com.impl.homesecurity.actuator;

import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.HomeSecurityApplication;
import com.impl.homesecurity.security.JwtAuthenticationFilter;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.impl.homesecurity.utils.JsonUtil.readJsonFile;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {HomeSecurityApplication.class}, value = {"server.port=8880"})
public class ActuatorEndpointsTest extends AbstractTestClass {

    private static final String ACTUATOR_ENDPOINT_RESPONSE_PATH = "src/test/resource/json/actuator-endpoint-response.json";
    private static final String ACTUATOR_METRICS_ENDPOINT_RESPONSE_PATH = "src/test/resource/json/actuator-metrics-endpoint-response.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .addFilters(jwtAuthenticationFilter)
                .build();
    }

    @Test
    public void whenGetActuatorEndpointThenReturnListOfAccessibleEndpoints() throws Exception {
        String response = this.mockMvc.perform(get("/actuator")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expected = readJsonFile(ACTUATOR_ENDPOINT_RESPONSE_PATH);
        assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    public void whenGetActuatorHealthEndpointThenReturnApplicationStatus() throws Exception {
        this.mockMvc.perform(get("/actuator/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetActuatorMetricsEndpointThenReturnResponseWithMetricsNames() throws Exception {
        String response = this.mockMvc.perform(get("/actuator/metrics/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expected = readJsonFile(ACTUATOR_METRICS_ENDPOINT_RESPONSE_PATH);
        assertEquals(expected, response, JSONCompareMode.LENIENT);
    }

    @Test
    public void whenGetConcreteActuatorMetricThenReturnMetricData() throws Exception {
        this.mockMvc.perform(get("/actuator/metrics/jvm.memory.max")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetNotExposedEndpointThenReturnStatusIsUnauthorized() throws Exception {
        this.mockMvc.perform(get("/actuator/info")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenGetInvalidEndpointThenReturnStatusInUnauthorized() throws Exception {
        this.mockMvc.perform(get("/actuator/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
