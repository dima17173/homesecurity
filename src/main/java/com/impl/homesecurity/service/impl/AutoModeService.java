package com.impl.homesecurity.service.impl;

import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.dao.SettingsDao;
import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.domain.*;
import com.impl.homesecurity.mapper.SettingsMapper;
import com.impl.homesecurity.service.dto.AutoModeDTO;
import com.impl.homesecurity.util.DateDeserializerUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import static com.impl.homesecurity.domain.enumeration.DayOfWeek.getCurrentDay;
import static com.impl.homesecurity.util.CommonUtils.recoverStringEncoding;

/**
 * Created by dima.
 * Creation currentTime 26.11.18.
 */
@Service
@Getter
public class AutoModeService {

    private final Logger log = LoggerFactory.getLogger(AutoModeService.class);
    private final SettingsDao settingsDao;
    private final DeviceDao deviceDao;
    @Value("${statusOn}")
    private String statusOn;
    @Value("${statusOff}")
    private String statusOff;
    private final FireBaseServiceImpl fireBaseService;
    private final UserDao userDao;

    @Autowired
    public AutoModeService(SettingsDao settingsDao, DeviceDao deviceDao, FireBaseServiceImpl fireBaseService, UserDao userDao) {
        this.settingsDao = settingsDao;
        this.deviceDao = deviceDao;
        this.fireBaseService = fireBaseService;
        this.userDao = userDao;
    }

    private void updateDeviceFromNewSettings(Settings settings, boolean action) {
        if (log.isInfoEnabled()) {
            log.info("Try to change setting status");
        }
        Device device = deviceDao.getDeviceById(settings.getDeviceId());
        device.setStatus(action);
        deviceDao.updateDevice(device);
        String deviceStatus = action ? getStatusOn() : getStatusOff();
        String messageText = device.getName() + " " + recoverStringEncoding(deviceStatus);
        Notification fireBaseNotification = new Notification("Auto Mode", messageText
                + " " + fireBaseService.getCurrentTime());
        List<User> userRoleDevices = userDao.getUsersByDeviceId(device.getId());
        for (User userRoleDevice : userRoleDevices) {
            if (userRoleDevice != null) {
                String topic = userRoleDevice.getId().toString();
                fireBaseService.sendPushNotification(topic, fireBaseNotification);
            }
        }
    }

    @Transactional
    public void performAutoMode(Settings settings, AutoMode autoMode) {
        if (log.isDebugEnabled()) {
            log.debug("Try to perform auto mode {} from settings {}", autoMode, settings);
        }
        Date date = new Date();
        LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime currentTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
        LocalTime autoModeTime = LocalDateTime.ofInstant(autoMode.getTime().toInstant(), ZoneId.systemDefault()).toLocalTime();
        int currentHours = currentTime.getHour();
        int currentMinutes = currentTime.getMinute();
        int autoModeHours = autoModeTime.getHour();
        int autoModeMinutes = autoModeTime.getMinute();
        int successfullyCompleted = autoMode.getSuccessfullyCompleted();

        for (Integer dayOfWeek : autoMode.getDayOfWeek()) {
            if (autoModeHours == currentHours && autoModeMinutes == currentMinutes && dayOfWeek.equals(getCurrentDay(date))) {
                if (autoMode.getRepeatable()) {
                    if (autoMode.getLastPerform() != null) {
                        LocalDate lastPerformLocalDate = autoMode.getLastPerform().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        if (currentDate.getYear() == lastPerformLocalDate.getYear() &&
                            currentDate.getMonth() == lastPerformLocalDate.getMonth() &&
                            currentDate.getDayOfMonth() == lastPerformLocalDate.getDayOfMonth()) {
                            if (log.isWarnEnabled()) {
                                log.warn("check year, month and day for update repeatable autoMode");
                            }
                            return;
                        }
                    }

                    updateDeviceFromNewSettings(settings, autoMode.getAction());
                    updateSetting(settings, autoMode, date, successfullyCompleted);
                    if (log.isInfoEnabled()) {
                        String status = "Last update repeatable autoMode";
                        if (autoMode.getAction()) {
                            log.info(status + " on " + date);
                        } else {
                            log.info(status + " off " + date);
                        }
                    }
                } else {
                    if (successfullyCompleted == 0) {
                        updateDeviceFromNewSettings(settings, autoMode.getAction());
                        updateSetting(settings, autoMode, date, successfullyCompleted);
                        if (log.isInfoEnabled()) {
                            String status = "Last update unrepeatable autoMode";
                            if (autoMode.getAction()) {
                                log.info(status + " on " + date);
                            } else {
                                log.info(status + " off " + date);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateSetting(Settings settings, AutoMode autoMode, Date date, int successfullyCompleted) {
        autoMode.setLastPerform(date);
        autoMode.setSuccessfullyCompleted(++successfullyCompleted);
        settings.setValue(autoMode.toJson());
        if (log.isInfoEnabled()) {
            log.info("Try to update setting");
        }
        settingsDao.updateSettings(settings);
        if (log.isInfoEnabled()) {
            log.info("Setting successfully updated");
        }
    }

    public JsonSettings mapToJsonSettings(String value) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializerUtil());

        Gson gson = gsonBuilder.create();
        return gson.fromJson(value, (Type) JsonSettings.class);
    }

    public AutoModeDTO autoModeToAutoModeDTO(Settings settings) {
        if (settings == null) {
            return null;
        } else {
            List<Settings> settingsList = settingsDao.getAllSettings();
            for (Settings currentSettingsList : settingsList) {
                JsonSettings jsonSettings = mapToJsonSettings(currentSettingsList.getValue());
                if (jsonSettings.getAutoMode() != null && (settings.getId().equals(currentSettingsList.getId()))) {
                    AutoMode autoMode = jsonSettings.getAutoMode();
                    return SettingsMapper.mapToAutoModeDTO(settings, autoMode);
                }
            }
            return null;
        }
    }
}
