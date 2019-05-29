package com.impl.homesecurity.service;

import com.google.firebase.messaging.Notification;
import com.impl.homesecurity.domain.SecurityNotification;

/**
 * Created by dima.
 * Creation date 22.11.18.
 */
public interface FireBaseService {

    void sendPushNotification(String topic, Notification fireBaseNotification);
    void sendMessage(String topic, SecurityNotification securityNotification);
}
