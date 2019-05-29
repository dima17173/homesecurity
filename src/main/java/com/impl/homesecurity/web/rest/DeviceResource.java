package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.domain.Device;
import com.impl.homesecurity.payload.ApiResponse;
import com.impl.homesecurity.security.CurrentUser;
import com.impl.homesecurity.security.UserPrincipal;
import com.impl.homesecurity.service.DeviceService;
import com.impl.homesecurity.util.HeaderUtil;
import com.impl.homesecurity.web.rest.errors.BadRequestAlertException;
import com.impl.homesecurity.web.rest.errors.BadRequestException;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DeviceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);
    private static final String ENTITY_NAME = "device";
    private final DeviceService deviceService;

    @GetMapping("/devices")
    @Timed
    public ResponseEntity<Device> getDevice(@RequestParam(name = "id") long id) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get Device : {}", id);
        }
        Device device = deviceService.getDeviceById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(device));
    }

    @GetMapping("/device")
    @Timed
    public List<Device> getAllDevices() {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get all devices");
        }
        return deviceService.getAllDevices();
    }

    @PostMapping("/device")
    @Timed
    public ResponseEntity<?> addDevice(@RequestBody Device device) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to add device : {}", device);
        }
        if (device.getId() != null)
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "id exists");
        if (deviceService.addDevice(device) > 0) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, device.toString())).build();
        } else
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/device")
    @Timed
    public ResponseEntity<?> updateDevices(@CurrentUser UserPrincipal user) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to update list devices for user: {}", user);
        }
        boolean updated = deviceService.updateDevices(user);
        if (updated){
            return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, user.toString())).build();
        } else {
            return new ResponseEntity<>(new ApiResponse(Boolean.FALSE, "Devices isn't valid"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
