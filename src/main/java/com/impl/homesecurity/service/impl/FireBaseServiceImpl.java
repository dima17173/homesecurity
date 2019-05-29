package com.impl.homesecurity.service.impl;

import com.google.firebase.messaging.*;
import com.impl.homesecurity.domain.SecurityNotification;
import com.impl.homesecurity.service.FireBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by dima.
 * Creation date 22.11.18.
 */
@Service
public class FireBaseServiceImpl implements FireBaseService {

    private final Logger log = LoggerFactory.getLogger(FireBaseServiceImpl.class);

    /**
     * Андроид конфиг для push notifications
     */
    private AndroidConfig androidConfig = AndroidConfig.builder()
            .setTtl(Duration.ofMinutes(60).toMillis())
            .setPriority(AndroidConfig.Priority.HIGH)
            .setNotification(AndroidNotification.builder()
                    .setSound("default")
                    .build())
            .build();

    //todo проверить ios config
    /**
     * iOS конфиг для push notifications
     */
    private ApnsConfig iosConfig = ApnsConfig.builder()
        .setAps(Aps.builder()
                .setSound("default")
                .build())
            .build();

    public void sendPushNotification(String topic, Notification fireBaseNotification) {
        log.info("Topic {} ", topic); //todo delete
        Message message = Message.builder()
                .setTopic(topic)
                .setAndroidConfig(androidConfig)
                .setApnsConfig(iosConfig)
                .setNotification(fireBaseNotification)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            if (log.isInfoEnabled()) {
                log.info("User send notification {} ", response);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String topic, SecurityNotification securityNotification) {
        log.info("Topic {} ", topic); //todo delete
        Message message = Message.builder()
                .putData("alarm", securityNotification.getMessage())
                .putData("uuid", securityNotification.getUuid().toString())
                .putData("createTime", Objects.toString(securityNotification.getCreateTime().getTime(), null))
                .putData("device", securityNotification.getDevice())
                .setTopic(topic)
                .build();
        String response;
        try {
            response = FirebaseMessaging.getInstance().sendAsync(message).get();
            if (log.isDebugEnabled()) {
                log.debug("Successfully sent message: " + securityNotification.getMessage()+ " " + response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public String getCurrentTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern( "uuuu-MM-dd HH:mm:ss" );
        return new Timestamp(System.currentTimeMillis()).toLocalDateTime().format(format);
    }
}
