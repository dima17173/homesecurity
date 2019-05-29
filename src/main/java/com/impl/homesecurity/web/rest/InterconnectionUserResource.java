package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.domain.MainUser;
import com.impl.homesecurity.service.InterconnectionUserService;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

/**
 * Created by dima.
 * Creation date 28.11.18.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class InterconnectionUserResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);
    private final InterconnectionUserService interconnectionUserService;

    @GetMapping("/getAllUsersDevicesByMainUser")
    @Timed
    public ResponseEntity<List<MainUser>> getInterconnectionUsersDevicesByMainUser(@RequestParam long id) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get InterconnectionUsers : {}", id);
        }
        List<MainUser> interconnectionUsers = interconnectionUserService.getAllUsersDevicesByMainUserId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(interconnectionUsers));
    }
}
