package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.domain.enumeration.Role;
import com.impl.homesecurity.service.RoleService;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by dima.
 * Creation date 27.11.18.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);
    private final RoleService roleService;

    @GetMapping("/role")
    @Timed
    public List<Role> getAllRoles() {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get all roles");
        }
        return roleService.getAllRoles();
    }

    @GetMapping("/roleById")
    @Timed
    public ResponseEntity<Role> getRoleById(@RequestParam(name = "id") Long id) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get Role : {}", id);
        }
        Role role = roleService.getRoleById(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(role));
    }
}
