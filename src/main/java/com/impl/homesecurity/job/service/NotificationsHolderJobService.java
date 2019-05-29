package com.impl.homesecurity.job.service;

import com.impl.homesecurity.domain.SecurityNotification;
import com.impl.homesecurity.service.impl.NotificationsHolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dima.
 * Creation date 20.12.18.
 */
@Service
public class NotificationsHolderJobService {

    private final Logger log = LoggerFactory.getLogger("Scheduled");
    private final NotificationsHolderService notificationsHolderService;
    private List<SecurityNotification> securityNotifications = new ArrayList<>();

    @Autowired
    public NotificationsHolderJobService(NotificationsHolderService notificationsHolderService) {
        this.notificationsHolderService = notificationsHolderService;
    }

    public void addNotification(SecurityNotification securityNotification) {
        notificationsHolderService.addNotification(securityNotification);
    }

    /**
     * Чистильщик событий, удаляет события из очереди старше 2х минут.
     */
    @Scheduled(fixedRate = 120000)
    public void notificationsCleaner() {
        if (log.isDebugEnabled()) {
            log.debug("Run notifications cleaner");
        }
        log.info("Run notifications cleaner. [{}]", securityNotifications.size());
        try {
            long currentTime = Instant.now().toEpochMilli();
            for (SecurityNotification securityNotification : new ArrayList<>(securityNotifications)) {
                if (currentTime - securityNotification.getCreateTime().getTime() > 120000) {
                    securityNotifications.remove(securityNotification);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Finish notifications cleaner");
            }
        } catch (SchedulingException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
        }
    }
}
