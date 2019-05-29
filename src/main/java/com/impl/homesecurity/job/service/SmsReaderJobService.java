package com.impl.homesecurity.job.service;

import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.service.impl.SmsReaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dima.
 * Creation date 20.12.18.
 */
@Service
@Profile("prod")
public class SmsReaderJobService {

    private final Logger log = LoggerFactory.getLogger("Scheduled");
    private final SmsReaderService smsReaderService;
    private final DeviceDao deviceDao;

    @Autowired
    public SmsReaderJobService(SmsReaderService smsReaderService, DeviceDao deviceDao) {
        this.smsReaderService = smsReaderService;
        this.deviceDao = deviceDao;
    }

    private Map<String, User> getUserMessages() {
        return smsReaderService.getUserMessages();
    }

    private Map<String, String> splitMessage(String message) {
        return smsReaderService.splitMessage(message);
    }

    private void updateDevices(List<Device> devices, User user) {
        smsReaderService.updateDevices(devices, user);
    }

    /**
     * Обработка списка всех SMS сообщений и логинов пользователей
     */
    @Scheduled(fixedRate = 61000)
    public void getDeviceFromMessage() {
        if (log.isDebugEnabled()) {
            log.debug("Try to get device number and status from messages every 61 seconds");
        }
        try {
            Map<String, User> usersMessages = getUserMessages();
            if (usersMessages != null && !usersMessages.isEmpty()) {
                List<Device> devices = new ArrayList<>();
                for (Map.Entry<String, User> userMessage : usersMessages.entrySet()) {
                    String message = userMessage.getKey();
                    Map<String, String> deviceStatuses = splitMessage(message);

                    for (Map.Entry<String, String> deviceStatus : deviceStatuses.entrySet()) {
                        String deviceNumber = deviceStatus.getKey();
                        String status = deviceStatus.getValue();

                        Device device = deviceDao.getDeviceByNumber(deviceNumber);
                        device.setStatus(Boolean.parseBoolean(status));

                        devices.add(device);
                    }
                    updateDevices(devices, userMessage.getValue());
                }
            }
        } catch (SchedulingException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
        }
    }
}
