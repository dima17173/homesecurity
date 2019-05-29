package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.domain.SmsSender;
import com.impl.homesecurity.service.impl.SmsSenderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/**
 * Created by diana.
 * Creation date 27.11.18.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SmsSenderResource {

    private final Logger log = LoggerFactory.getLogger(SmsReaderResource.class);
    private final SmsSenderService smsSenderService;

    @PostMapping("/sms/sent")
    @Timed
    public void sentMessages(@RequestBody SmsSender smsSender) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("REST request to sent message : {}", smsSender);
        }
        smsSenderService.sentResponse(smsSender);
    }
}
