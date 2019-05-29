package com.impl.homesecurity.controller;

import com.impl.homesecurity.AbstractTestClass;
import com.impl.homesecurity.dao.SettingsDao;
import com.impl.homesecurity.service.dto.AutoModeSaveDTO;
import com.impl.homesecurity.service.dto.SettingsSaveDTO;
import com.impl.homesecurity.service.impl.AutoModeService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;

/**
 * Created by dima.
 * Creation date 04.02.19.
 */
public class TestAutoMode extends AbstractTestClass {

    @Autowired
    private AutoModeService autoModeService;
    @Autowired
    private SettingsDao settingsDao;

    private SettingsSaveDTO settingsSaveDTO;

    private static long lastUpdate = Instant.now().toEpochMilli();


    public SettingsSaveDTO createSettingSaveDTO() {
        AutoModeSaveDTO autoModeSaveDTO = new AutoModeSaveDTO();
        autoModeSaveDTO.setAction(true);
        autoModeSaveDTO.setRepeatable(false);
        autoModeSaveDTO.setLastUpdate(lastUpdate);
        autoModeSaveDTO.setTime("16:50");
        autoModeSaveDTO.setStatus(true);
        autoModeSaveDTO.setDayOfWeek(Arrays.asList(1,2,3,4,5,6,0));

        SettingsSaveDTO settingsSaveDTO = new SettingsSaveDTO();
        settingsSaveDTO.setDeviceId(5L);
        settingsSaveDTO.setUserId(7L);
        settingsSaveDTO.setValue(autoModeSaveDTO);

        return settingsSaveDTO;
    }

    @Before
    public  void setUp() {
        settingsSaveDTO = createSettingSaveDTO();
    }

    @Test
    public void performAutoModeWithActionTrueAndRepeatableFalse() {

        Clock clock = Clock.fixed(Instant.parse("16:50"), ZoneId.of("UTC"));
        LocalDateTime dateTime = LocalDateTime.now(clock);

        AutoModeSaveDTO autoModeSaveDTO = new AutoModeSaveDTO();
        autoModeSaveDTO.setAction(true);
        autoModeSaveDTO.setRepeatable(false);
        autoModeSaveDTO.setLastUpdate(lastUpdate);
        autoModeSaveDTO.setTime("16:50");
        autoModeSaveDTO.setStatus(true);
        autoModeSaveDTO.setDayOfWeek(Arrays.asList(1,2,3,4,5,6,0));

        SettingsSaveDTO settingsSaveDTO = new SettingsSaveDTO();
        settingsSaveDTO.setDeviceId(5L);
        settingsSaveDTO.setUserId(7L);
        settingsSaveDTO.setValue(autoModeSaveDTO);

        assertEquals(settingsSaveDTO, createSettingSaveDTO());
    }

    @Test
    public void performAutoModeWithActionFalseAndRepeatableFalse() {

        AutoModeSaveDTO autoModeSaveDTO = new AutoModeSaveDTO();
        autoModeSaveDTO.setAction(false);
        autoModeSaveDTO.setRepeatable(false);
        autoModeSaveDTO.setLastUpdate(lastUpdate);
        autoModeSaveDTO.setTime("16:50");
        autoModeSaveDTO.setStatus(true);
        autoModeSaveDTO.setDayOfWeek(Arrays.asList(1,2,3,4,5,6,0));

        SettingsSaveDTO settingsSaveDTO = new SettingsSaveDTO();
        settingsSaveDTO.setDeviceId(5L);
        settingsSaveDTO.setUserId(7L);
        settingsSaveDTO.setValue(autoModeSaveDTO);
    }

}