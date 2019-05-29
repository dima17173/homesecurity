package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.domain.Mote;
import com.impl.homesecurity.service.MoteService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MoteResource {

    private final Logger log = LoggerFactory.getLogger(MoteResource.class);
    private final MoteService moteService;

    @GetMapping("/freeDevice")
    @Timed
    public List<Mote> getAllFreeDevice(){
        if (log.isDebugEnabled()) {
            log.debug("REST request to get all free devices");
        }
        return moteService.getAllFreeDevice();
    }
}
