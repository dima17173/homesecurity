package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.dao.DeviceDao;
import com.impl.homesecurity.dao.UserDao;
import com.impl.homesecurity.domain.ClientMessage;
import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.domain.User;
import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import com.impl.homesecurity.service.dto.SmsServiceResponseDTO;
import com.impl.homesecurity.web.rest.errors.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.impl.homesecurity.util.CommonUtils.recoverStringEncoding;

@Service
@Profile("prod")
public class SmsReaderService {

    private final Logger log = LoggerFactory.getLogger(SmsReaderService.class);
    private final HttpClientService httpClientService;
    private final UserDao userDao;
    private final DeviceDao deviceDao;
    private long lastCheckedMessageId = 0;
    private final DeviceServiceImpl deviceService;

    @Autowired
    public SmsReaderService(HttpClientService httpClientService, UserDao userDao, DeviceDao deviceDao,
                            DeviceServiceImpl deviceService) {
        this.httpClientService = httpClientService;
        this.userDao = userDao;
        this.deviceDao = deviceDao;
        this.deviceService = deviceService;
    }

    /**
     * Получение списка всех SMS сообщений и логинов пользователей
     * @return
     */
    public Map<String, User> getUserMessages() {
        if (log.isDebugEnabled()) {
            log.debug("Request to get all user messages");
        }
        SmsServiceResponseDTO smsResponse = httpClientService.getRequest();
        if (smsResponse.getClientMessages() != null) {
            Map<String, User> usersMessages = new HashMap<>();

            for (ClientMessage clientMessage : smsResponse.getClientMessages()) {
                if (clientMessage.getId() > lastCheckedMessageId) {
                    User user = userDao.getUserByLogin(clientMessage.getPhone());
                    String message = clientMessage.getMessage();

                    usersMessages.put(message, user);
                    lastCheckedMessageId = clientMessage.getId();
                }
            }
            if (log.isInfoEnabled()) {
                log.info("Users messages length : {}", usersMessages.size());
            }
            return usersMessages;
        } else {
            if (log.isErrorEnabled()) {
                log.error("Unable get user messages with error : {}", smsResponse.getResponseError());
            }
            throw new BadRequestException(smsResponse.getResponseError().toString());
        }
    }

    /**
     * Получение из строки сообщения SMS номера девайса и желаемый статус
     * @param message
     * @return
     */
    public Map<String, String> splitMessage(String message) {

        Map<String, String> deviceStatus = new HashMap<>();
        String[] messageDevices = message.split(";");

        for (String device : messageDevices) {
            String deviceNumber = StringUtils.substringBetween(device, "number_", "status_");
            String status = StringUtils.substringAfter(device, "status_");
            deviceStatus.put(deviceNumber, status);
        }
        return deviceStatus;
    }

    /**
     * Обновление статуса устройства через SMS сообщения
     * @param devices устройства
     * @param user пользователь
     */
    public void updateDevices(List<Device> devices, User user) {
        if (log.isDebugEnabled()) {
            log.debug("Request to update devices {} from message", devices);
        }
        if (userDao.existsByUsername(user.getLogin())) {
            if (devices != null && !devices.isEmpty()) {
                if (deviceDao.getAllDevicesByUserId(user.getId())
                        .stream()
                        .map(Device::getId)
                        .collect(Collectors.toList())
                        .containsAll(devices.stream()
                                .map(Device::getId)
                                .collect(Collectors.toList()))) {

                    int[] status = deviceDao.updateDeviceAndInverseStatus(devices);
                    if (status.length > 0) {
                        String deviceName = devices.get(0).getName();
                        String action = devices.get(0).isStatus() ?
                                recoverStringEncoding(deviceService.getDeviceOn()) :
                                recoverStringEncoding(deviceService.getDeviceOff());
                        deviceService.sendNotificationWithDeviceStatus(devices, user, deviceName, action);
                    }
                }
            } else {
                if (log.isErrorEnabled()) {
                    log.error("Device is not valid");
                }
                throw new BadRequestException(ExceptionCodes.DEVICE_DOES_NOT_EXIST.getMsg());
            }
        }  else {
            if (log.isErrorEnabled()) {
                log.error("User with id : {} doesn't exist", user.getId());
                throw new BadRequestException(ExceptionCodes.USER_DOES_NOT_EXIST.getMsg());
            }
        }
    }
}
