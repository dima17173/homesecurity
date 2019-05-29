package com.impl.homesecurity.service;

import com.impl.homesecurity.domain.SettingUser;
import com.impl.homesecurity.domain.Settings;
import java.util.List;

/**
 * Created by diana.
 * Creation date 22.11.18.
 */
public interface SettingsService {

    int addSettings(Settings settings);
    int updateSettings(Settings settings);
    int deleteSettings(long id);
    List<Settings> getAllSettings();
    Settings getSettingsById(long settingsId);
    Settings getSettingsByUserId(long userId);
    List<SettingUser> getSettingAndUserNameByDeviceId(long deviceId);
}
