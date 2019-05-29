package com.impl.homesecurity.dao;

import com.impl.homesecurity.domain.SettingUser;
import com.impl.homesecurity.domain.Settings;
import java.util.List;

/**
 * Created by dima.
 * Creation date 23.10.18.
 */
public interface SettingsDao {

    int addSettings(Settings settings);
    int updateSettings(Settings settings);
    int deleteSettings(long id);
    List<Settings> getAllSettings();
    Settings getSettingsById(long settingsId);
    Settings getSettingsByUserId(long userId);
    List<SettingUser> getSettingAndUserNameByDeviceId(long deviceId);
}
