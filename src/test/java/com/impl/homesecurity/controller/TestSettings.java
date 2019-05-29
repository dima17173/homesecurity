package com.impl.homesecurity.controller;

import com.google.gson.Gson;
import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.domain.Settings;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.service.SettingsService;
import com.impl.homesecurity.service.UserService;
import com.impl.homesecurity.service.dto.AutoModeDTO;
import com.impl.homesecurity.service.dto.AutoModeSaveDTO;
import com.impl.homesecurity.service.dto.SettingsSaveDTO;
import com.impl.homesecurity.service.impl.AutoModeService;
import com.impl.homesecurity.web.rest.SettingsResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dima.
 * Creation date 11.12.18.
 */
public class TestSettings extends AbstractTestClass {

    @Autowired
    private Gson gson;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private SettingsResource settingsResource;
    @Autowired
    private SettingsService settingsService;
    @Autowired
    private UserService userService;
    @Autowired
    private AutoModeService autoModeService;

    private User user;
    private Settings settings;
    private String token;
    private AutoModeDTO autoModeDTO;
    private SettingsSaveDTO settingsSaveDTO;

    public Settings createSettings() {
        String value = "{\"autoMode\":{\"time\":\"16:26\", \"repeatable\":true, \"action\":false, \"status\":true, \"dayOfWeek\":[1, 2, 3, 4, 5, 6, 0], \"lastUpdate\":\"1549288963814\"}}";
        Settings settings = new Settings();
        settings.setDeviceId(5L);
        settings.setUserId(user.getId());
        settings.setValue(value);
        return settings;
    }

    public SettingsSaveDTO createSettingsSaveDTO() {
        AutoModeSaveDTO autoModeSaveDTO = new AutoModeSaveDTO();
        autoModeSaveDTO.setAction(true);
        autoModeSaveDTO.setRepeatable(true);
        autoModeSaveDTO.setLastUpdate(Instant.now().toEpochMilli());
        autoModeSaveDTO.setTime("10:10");
        autoModeSaveDTO.setStatus(true);
        autoModeSaveDTO.setDayOfWeek(Arrays.asList(1,2,3,4,5,6,0));
        SettingsSaveDTO settingsSaveDTO = new SettingsSaveDTO();
        settingsSaveDTO.setDeviceId(5L);
        settingsSaveDTO.setUserId(7L);
        settingsSaveDTO.setValue(autoModeSaveDTO);
        return settingsSaveDTO;
    }

    @Before
    public void setUp() {
        token = getToken();
        user = createUser();
        userService.addUser(user);
        user = userService.getUserByLogin(user.getLogin());
        settings = createSettings();
        settingsService.addSettings(settings);
        settings = settingsService.getSettingsByUserId(settings.getUserId());
        autoModeDTO = autoModeService.autoModeToAutoModeDTO(settings);
        settingsSaveDTO = createSettingsSaveDTO();
    }

    @After
    public void clean() {
        if (settings != null) {
            settingsService.deleteSettings(settings.getId());
        }
        if (user != null) {
            userService.deleteUser(user.getId());
        }
    }

    @Test
    public void getSettingById() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/setting?id={id}", settings.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(autoModeDTO)))
                .andReturn();
        String str = result.getResponse().getContentAsString();
        assertTrue(str.contains("\"id\":" + settings.getId()));
    }

    @Test
    public void deleteSettings() throws Exception {
        int dataBaseSizeBeforeDelete = settingsService.getAllSettings().size();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/settings?id={id}", settings.getId())
                .header("Authorization", token))
                .andExpect(status().isOk());
        settings = null;
        List<Settings> settingsList = settingsService.getAllSettings();
        assertThat(settingsList).hasSize(dataBaseSizeBeforeDelete - 1);
    }

    @Test
    public void updateSettings() throws Exception {
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/api/settings")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(settingsSaveDTO));
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .isOk());
    }

    @Test
    public void addSetting() throws Exception {
        User user = createUser();
        user.setName("UpdateUser");
        user.setLogin("380001112244");
        userService.addUser(user);
        user = userService.getUserByLogin(user.getLogin());
        Long userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/settings")
                .header("Authorization", token)
                .content("{\"deviceId\":9,\"userId\":"+userId+"," +
                        "\"value\": {\"autoMode\":{\"time\":\"16:50\", \"repeatable\":true, \"action\":true, \"status\":true, \"dayOfWeek\":[0, 1, 2, 3, 4, 5, 6], \"lastUpdate\":1549617974724}}}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Settings settingTest = settingsService.getSettingsByUserId(user.getId());

        assertEquals(userId, settingTest.getUserId());
        settingsService.deleteSettings(settingTest.getId());
        userService.deleteUser(user.getId());
    }
}
