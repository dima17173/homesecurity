package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.job.service.AutoModeJobService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by diana.
 * Creation date 26.11.18.
 */
@RestController
@RequestMapping("/api")
@Profile("dev")
@AllArgsConstructor
public class AutoModeResource {

    private final AutoModeJobService autoModeJobService;

    @PutMapping("/settings/changeStatus")
    @Timed
    public void changeStatus() {
        autoModeJobService.getNewSettings();
    }
}
