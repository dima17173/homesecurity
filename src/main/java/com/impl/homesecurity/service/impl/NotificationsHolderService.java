package com.impl.homesecurity.service.impl;

import com.impl.homesecurity.domain.SecurityNotification;
import com.impl.homesecurity.domain.SmsSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис обработки событий связаных с отправкой пуш нотификейшена в файр бейс и проверки ответа для последующей отправки SMS
 */
@Service
public class NotificationsHolderService {
    private final Logger log = LoggerFactory.getLogger(NotificationsHolderService.class);
    private final SmsSenderService smsSenderService;
    private List<SecurityNotification> securityNotifications = new ArrayList<>();
    
    @Autowired
    public NotificationsHolderService(SmsSenderService smsSenderService) {
        this.smsSenderService = smsSenderService;
    }

    /**
     * Метод добавления события в очередь событий, приходящих в ответ на пуш нотификейшн файрбейс.
     * @param securityNotification Событие, приходящее в ответ на пуш нотификейшн.
     */
    public void addNotification(SecurityNotification securityNotification) {
        if (log.isDebugEnabled()) {
            log.debug("Request to add notification {}", securityNotification);
        }
        this.securityNotifications.add(securityNotification);
        log.info("Add securityNotification {}", securityNotification);
        if (log.isDebugEnabled()) {
            log.debug("Notification {} added", securityNotification);
        }
    }

    /**
     * Процесс проверки ответа на пуш нотификейшн из файрбейс. 
     * Активируется паралельно отправке пуш нотификейшена, 
     * и ожидает 1мин 30сек перед проверкой ответа от мобильного приложения.
     * @param securityNotificationForCheck Проверяемое событие которое послано в пуш нотификейш.
     * @param phone Логин юзера.
     */
    void checkNotification(SecurityNotification securityNotificationForCheck, String phone) {
        if (log.isDebugEnabled()) {
            log.debug("Run Check Notification {} for user {}", securityNotificationForCheck, phone);
        }
        Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(90000);
                        boolean isPresent = false;
                        for (SecurityNotification notification : this.securityNotifications) {
                            if (notification.getUuid().equals(securityNotificationForCheck.getUuid())
                                    && notification.getCreateTime().equals(securityNotificationForCheck.getCreateTime())) {
                                isPresent = true;
                            }
                        }
                        if (!isPresent) {
                            smsSenderService.sentResponse(new SmsSender(phone, securityNotificationForCheck.toText()));
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("Finish Check Notification {}", securityNotificationForCheck);
                        }
                    } catch (InterruptedException e) {
                        if (log.isErrorEnabled()) {
                            log.error("Thread sleep exception");
                        }
                    } catch (IOException e) {
                        if (log.isErrorEnabled()) {
                            log.error("Can't send response to user {} with text {}", phone, securityNotificationForCheck);
                        }
                    }
        });
        thread.start();
    }
}
