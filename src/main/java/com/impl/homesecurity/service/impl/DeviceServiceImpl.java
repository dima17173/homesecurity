package com.impl.homesecurity.service.impl;

import com.google.firebase.messaging.Notification;
import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.security.UserPrincipal;
import com.impl.homesecurity.service.DeviceService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import static com.impl.homesecurity.util.CommonUtils.recoverStringEncoding;

@Getter
@Service
public class DeviceServiceImpl implements DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceServiceImpl.class);
    private final DeviceDao deviceDao;
    private final UserDao userDao;
    private final FireBaseServiceImpl fireBaseService;
    @Value("${deviceOn}")
    private String deviceOn;
    @Value("${deviceOff}")
    private String deviceOff;
    @Value("${notification}")
    private String notification;

    @Autowired
    public DeviceServiceImpl(DeviceDao deviceDao, UserDao userDao, FireBaseServiceImpl fireBaseService) {
        this.deviceDao = deviceDao;
        this.userDao = userDao;
        this.fireBaseService = fireBaseService;
    }

    @Override
    public Device getDeviceById(long id) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get device with id = {}", id);
        }
        return deviceDao.getDeviceById(id);
    }

    @Override
    public List<Device> getAllDevices() {
        if (log.isDebugEnabled()) {
            log.debug("Request to get all devices");
        }
        return deviceDao.getAllDevices();
    }

    @Override
    public int addDevice(Device device) {
        if (log.isDebugEnabled()) {
            log.debug("Request to add device : {}", device);
        }
        return deviceDao.addDevice(device);
    }

    @Override
    public Device getDeviceByNumber(String number) {
        if (log.isDebugEnabled()) {
            log.debug("Request to get Device by number = : {}", number);
        }
        return deviceDao.getDeviceByNumber(number);
    }

    @Override
    public boolean updateDevices(UserPrincipal user) {
        if (log.isDebugEnabled()) {
            log.debug("Request to update list of devices for user : {}");
        }
        List<Device> actualDevices = deviceDao.getAllDevicesByUserId(user.getId());
        if (!CollectionUtils.isEmpty(actualDevices)) {
            int[] status = deviceDao.updateDeviceAndInverseStatus(actualDevices);

            if (status.length > 0) {
                User userPrincipal = userDao.getUserByLogin(user.getUsername());

                String deviceName = actualDevices.get(0).getName();
                String action = actualDevices.get(0).isStatus() ? recoverStringEncoding(getDeviceOff()) : recoverStringEncoding(getDeviceOn());

                sendNotificationWithDeviceStatus(actualDevices, userPrincipal, deviceName, action);

            }
            if (log.isInfoEnabled()) {
                log.info("User is trying to update devices with status: {}", status);
            }
            return status.length > 0;
        } else {
            if (log.isWarnEnabled()) {
                log.warn("User doesn't have expected devices {}", actualDevices);
            }
            return false;
        }
    }

    public void sendNotificationWithDeviceStatus(List<Device> actualDevices, User userPrincipal, String deviceName, String action) {
        Notification fireBaseNotification = new Notification(recoverStringEncoding(getNotification()),
                userPrincipal.getName() + " " + action + deviceName
                        + " " + fireBaseService.getCurrentTime());

        List<User> userRoleDevices = userDao.getUsersByDeviceId(actualDevices.get(0).getId());
        new Thread(() -> {
            for (User userRoleDevice : userRoleDevices) {
                try {
                    if (userRoleDevice != null) {
                        String topic = userRoleDevice.getId().toString();
                        fireBaseService.sendPushNotification(topic, fireBaseNotification);
                    }
                } catch (Exception e) {
                    log.error(String.format("Could not send notification to user[%s]", userRoleDevice));
                }
            }
        }).start();
    }
}
