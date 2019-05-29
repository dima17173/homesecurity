package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.domain.SecurityNotification;
import com.impl.homesecurity.job.service.NotificationsHolderJobService;
import com.impl.homesecurity.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер приема ответов из мобильного приложения на пуш нотификейшн файрбейс
 */
@RestController
@RequestMapping("/api")
public class NotificationResource {
    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);
    private final NotificationsHolderJobService notificationsHolderJobService;
    private static final String ENTITY_NAME = "notification";

    @Autowired
    public NotificationResource(NotificationsHolderJobService notificationsHolderJobService) {
        this.notificationsHolderJobService = notificationsHolderJobService;
    }

    @PostMapping("/sendNotification")
    @Timed
    public ResponseEntity<?> sendNotifications(@RequestBody SecurityNotification securityNotification) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to send notification : {}", securityNotification);
        }
        if (securityNotification != null) {
            notificationsHolderJobService.addNotification(securityNotification);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
                    securityNotification.getUuid().toString())).build();
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
