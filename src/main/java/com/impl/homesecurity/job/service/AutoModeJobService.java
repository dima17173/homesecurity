package com.impl.homesecurity.job.service;

import com.impl.homesecurity.domain.AutoMode;
import com.impl.homesecurity.domain.JsonSettings;
import com.impl.homesecurity.domain.Settings;
import com.impl.homesecurity.service.SettingsService;
import com.impl.homesecurity.service.impl.AutoModeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by dima.
 * Creation date 20.12.18.
 */
@Service
@Profile("prod")
public class AutoModeJobService {

    private final Logger log = LoggerFactory.getLogger("Scheduled");
    private final AutoModeService autoModeService;
    private final SettingsService settingsService;

    @Autowired
    public AutoModeJobService(AutoModeService autoModeService, SettingsService settingsService) {
        this.autoModeService = autoModeService;
        this.settingsService = settingsService;
    }

    private void performAutoMode(Settings settings, AutoMode autoMode) {
        autoModeService.performAutoMode(settings, autoMode);
    }

    private JsonSettings convertJsonToObject(String value) {
        return autoModeService.mapToJsonSettings(value);
    }

    @Scheduled(fixedRate = 15000)
    public void getNewSettings() {
        if (log.isDebugEnabled()) {
            log.debug("Try to get all settings");
        }
        try {
            List<Settings> settingsList = settingsService.getAllSettings();
            for (Settings currentSettingsList : settingsList) {
                JsonSettings jsonSettings = convertJsonToObject(currentSettingsList.getValue());
                if (jsonSettings.getAutoMode() != null) {
                    AutoMode autoMode = jsonSettings.getAutoMode();
                    if(autoMode.getStatus()){
                        performAutoMode(currentSettingsList, autoMode);
                    }
                }
            }
        } catch (SchedulingException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
        }
    }
}
