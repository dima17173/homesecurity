package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.dao.SettingsDao;
import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.domain.SettingUser;
import com.impl.homesecurity.domain.Settings;
import com.impl.homesecurity.service.SettingsService;
import com.impl.homesecurity.web.rest.errors.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by diana.
 * Creation date 22.11.18.
 */
@Service
public class SettingsServiceImpl implements SettingsService {

    private final Logger log = LoggerFactory.getLogger(SettingsServiceImpl.class);
    private final SettingsDao settingsDao;
    private final UserDao userDao;
    private final DeviceDao deviceDao;

    @Autowired
    public SettingsServiceImpl(SettingsDao settingsDao,DeviceDao deviceDao, UserDao userDao) {
        this.settingsDao = settingsDao;
        this.deviceDao = deviceDao;
        this.userDao = userDao;
    }

    @Override
    public int addSettings(Settings settings) {
        if (log.isDebugEnabled()) {
            log.debug("Request to add settings, settings = {}", settings);
        }
        return settingsDao.addSettings(settings);
    }

    @Override
    public int updateSettings(Settings settings) {
        if (log.isDebugEnabled()) {
            log.debug("Request to update settings, settings = {}", settings);
        }
        return settingsDao.updateSettings(settings);
    }

    @Override
    public int deleteSettings(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Request to delete settings with id = {}", id);
        }
        return settingsDao.deleteSettings(id);
    }

    @Override
    public List<Settings> getAllSettings() {
        if (log.isDebugEnabled()) {
            log.debug("Request to get all settings");
        }
        return settingsDao.getAllSettings();
    }

    @Override
    public Settings getSettingsById(long settingsId) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get settings with id = {}", settingsId);
        }
        return settingsDao.getSettingsById(settingsId);
    }

    @Override
    public Settings getSettingsByUserId(long userId) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get settings with user id {}", userId);
        }
        if (userDao.getUserById(userId) != null) {
              return settingsDao.getSettingsByUserId(userId);
        } else {
            throw new BadRequestException("user with id = " + userId + " does not exist");
        }
    }

    @Override
    public List<SettingUser> getSettingAndUserNameByDeviceId(long deviceId) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get settings with device id {}", deviceId);
        }
        if(deviceDao.getDeviceById(deviceId) != null) {
            return settingsDao.getSettingAndUserNameByDeviceId(deviceId);
        }else {
            throw new BadRequestException("device with id = " + deviceId + " does not exist");
        }
    }
}
