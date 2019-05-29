package com.impl.homesecurity.web.rest;

import com.impl.homesecurity.job.service.AppDataJobService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by diana.
 * Creation date 22.11.18.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AppDataResource {

    private final Logger log = LoggerFactory.getLogger(AppDataResource.class);
    private final AppDataJobService appDataJobService;

    @GetMapping("/appData")
    public void getDataFromAppData() {
        if (log.isDebugEnabled()) {
            log.debug("try get data from AppData");
        }
        appDataJobService.getDataFromAppData();
    }
}
