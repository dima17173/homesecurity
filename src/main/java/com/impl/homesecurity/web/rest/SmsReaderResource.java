package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.job.service.SmsReaderJobService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Profile("dev")
@AllArgsConstructor
public class SmsReaderResource {

    private final Logger log = LoggerFactory.getLogger(SmsReaderResource.class);
    private final SmsReaderJobService smsReaderJobService;

    @PutMapping("/sms/device")
    @Timed
    public void updateDevices() {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get update devices from sms");
        }
        smsReaderJobService.getDeviceFromMessage();
    }
}
