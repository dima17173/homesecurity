package com.impl.homesecurity.mapper;

import com.impl.homesecurity.domain.AutoMode;
import com.impl.homesecurity.domain.Settings;
import com.impl.homesecurity.service.dto.AutoModeDTO;
import com.impl.homesecurity.service.dto.SettingsSaveDTO;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 * Created by dima.
 * Creation date 25.12.18.
 */
public class SettingsMapper {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

    public static AutoModeDTO mapToAutoModeDTO(Settings settings, AutoMode autoMode) {
        Assert.notNull(settings, "object can't be null");
        Assert.notNull(autoMode, "object can't be null");
        AutoModeDTO autoModeDTO = new AutoModeDTO();
        autoModeDTO.setId(settings.getId());
        autoModeDTO.setUserId(settings.getUserId());
        autoModeDTO.setDeviceId(settings.getDeviceId());
        autoModeDTO.setTime(simpleDateFormat.format(autoMode.getTime()));
        autoModeDTO.setSuccessfullyCompleted(autoMode.getSuccessfullyCompleted());
        autoModeDTO.setAction(autoMode.getAction());
        autoModeDTO.setStatus(autoMode.getStatus());
        autoModeDTO.setRepeatable(autoMode.getRepeatable());
        autoModeDTO.setDayOfWeek(autoMode.getDayOfWeek());
        return autoModeDTO;
    }

    public static Settings mapToDTO(SettingsSaveDTO settingsSaveDTO) {
        Assert.notNull(settingsSaveDTO, "object can't be null");
        Assert.notNull(settingsSaveDTO.getValue(), "AutoMode can't be null");
        settingsSaveDTO.getValue().setLastUpdate(Instant.now().toEpochMilli());
        Settings settings = new Settings();
        settings.setId(settingsSaveDTO.getId());
        settings.setUserId(settingsSaveDTO.getUserId());
        settings.setDeviceId(settingsSaveDTO.getDeviceId());
        settings.setValue(settingsSaveDTO.getValue().toString());
        return settings;
    }
}
