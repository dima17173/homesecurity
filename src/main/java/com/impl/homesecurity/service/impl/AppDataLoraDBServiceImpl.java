package com.impl.homesecurity.service.impl;

import com.google.firebase.messaging.Notification;
import com.impl.homesecurity.dao.AppDataDaoLoraDB;
import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.domain.*;
import com.impl.homesecurity.service.AppDataLoraDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by dima.
 * Creation date 22.11.18.
 */
@Service
public class AppDataLoraDBServiceImpl implements AppDataLoraDBService {

    private final Logger log = LoggerFactory.getLogger(AppDataLoraDBServiceImpl.class);
    private final DeviceDao deviceDao;
    private final UserDao userDao;
    private final NotificationsHolderService notificationsHolderService;
    private final AppDataDaoLoraDB appDataDaoLoraDB;
    private final FireBaseServiceImpl fireBaseService;


    @Autowired
    public AppDataLoraDBServiceImpl(DeviceDao deviceDao,
                                    UserDao userDao,
                                    NotificationsHolderService notificationsHolderService,
                                    AppDataDaoLoraDB appDataDaoLoraDB,
                                    FireBaseServiceImpl fireBaseService) {
        this.deviceDao = deviceDao;
        this.userDao = userDao;
        this.notificationsHolderService = notificationsHolderService;
        this.appDataDaoLoraDB = appDataDaoLoraDB;
        this.fireBaseService = fireBaseService;
    }

    @Override
    public List<AppData> getAllDeviceSignalsLoraDB() {
        if (log.isDebugEnabled()) {
            log.debug("Try get all AppData");
        }
        return appDataDaoLoraDB.getAllDeviceSignalsLoraDB();
    }

    @Override
    public AppData getLastDeviceSignalLoraDB() {
        if (log.isDebugEnabled()) {
            log.debug("Try get last device signal from LoraDB app data");
        }
        return appDataDaoLoraDB.getLastDeviceSignalLoraDB();
    }

    @Override
    public List<AppData> getIntervalAppDataLoraDBById(long fromId, long toId) {
        if (log.isDebugEnabled()) {
            log.debug("Try get all AppData from id = " + fromId + " to id = " + toId);
        }
        return appDataDaoLoraDB.getIntervalAppDataLoraDBById(fromId, toId);
    }

    @Override
    public List<AppData> getIntervalAppDataByDate(Timestamp fromDate, Timestamp toDate) {
        if (log.isDebugEnabled()) {
            log.debug("Try get all AppData from data = " + fromDate + " to data = " + toDate);
        }
        return appDataDaoLoraDB.getIntervalAppDataByDate(fromDate, toDate);
    }

    /**
     * Отправляем уведомление пользователю, если события происходили при статусе девайса "на сигнализации"
     * @param appData Сигнал от датчика
     */
    @Override
    public void filterDataByDeviceStatus(AppData appData) {
        if (log.isDebugEnabled()) {
            log.debug("Try to check if device {} status = on", appData.getMote());
        }
        String mote = appData.getMote().toString();
        Device device = deviceDao.getDeviceByNumber(mote);
        if (device != null) {
            if (device.isStatus()) {
                List<User> userRoleDevices = userDao.getUsersByDeviceId(device.getId());
                for (User userRoleDevice : userRoleDevices) {
                    if (userRoleDevice != null) {
                        String topic = userRoleDevice.getId().toString();
                        String messageText = "Сработала  сигнализация!";
                        Notification fireBaseNotification = new Notification(messageText,
                                messageText + " На устройстве: " + device.getName()
                                        + " " + fireBaseService.getCurrentTime());
                        SecurityNotification securityNotification = new SecurityNotification(userRoleDevice.getId(),
                                messageText, device.getName());

                        notificationsHolderService.checkNotification(securityNotification, userRoleDevice.getLogin());
                        fireBaseService.sendPushNotification(topic, fireBaseNotification);
                        fireBaseService.sendMessage(topic, securityNotification);
                    }
                }
            }
        }
    }
}
