package com.impl.homesecurity.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.impl.homesecurity.domain.SettingUser;
import com.impl.homesecurity.domain.Settings;
import com.impl.homesecurity.mapper.SettingsMapper;
import com.impl.homesecurity.service.SettingsService;
import com.impl.homesecurity.service.dto.AutoModeDTO;
import com.impl.homesecurity.service.dto.SettingsSaveDTO;
import com.impl.homesecurity.service.impl.AutoModeService;
import com.impl.homesecurity.util.HeaderUtil;
import com.impl.homesecurity.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by dima.
 * Creation date 07.12.18.
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class SettingsResource {

    private final Logger log = LoggerFactory.getLogger(SettingsResource.class);
    private static final String ENTITY_NAME = "settings";
    private final SettingsService settingsService;
    private final AutoModeService autoModeService;

    @PostMapping("/settings")
    @Timed
    public ResponseEntity createSettings(@RequestBody SettingsSaveDTO settingsSaveDTO) {
         if (log.isDebugEnabled()) {
            log.debug("REST request to save Settings : {}", settingsSaveDTO);
        }
        Settings settings = SettingsMapper.mapToDTO(settingsSaveDTO);
        if (settings.getId() != null) {
            throw new BadRequestAlertException("A new settings cannot already have an ID", ENTITY_NAME, "id exists");
        } else if (settingsService.addSettings(settings) > 0) {

            return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME,
                    settings.toString())).build();
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/settings")
    @Timed
    public ResponseEntity updateSettings(@RequestBody SettingsSaveDTO settingsSaveDTO) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to update Settings : {}", settingsSaveDTO);
        }
        Settings settings = SettingsMapper.mapToDTO(settingsSaveDTO);
        if (settingsSaveDTO.getId() == null) {
            return createSettings(settingsSaveDTO);
        } else if (settingsService.updateSettings(settings) > 0) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
                    settingsSaveDTO.getId().toString())).build();
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/settings")
    @Timed
    public List<Settings> getAllSettings() {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get all Settings");
        }
        return settingsService.getAllSettings();
    }

    @GetMapping("/setting")
    @Timed
    public ResponseEntity<AutoModeDTO> getSettingsById(@RequestParam Long id) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get Settings : {}", id);
        }
        Settings settings = settingsService.getSettingsById(id);
        AutoModeDTO responseDTO = autoModeService.autoModeToAutoModeDTO(settings);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(responseDTO));
    }

    @GetMapping("/settings/users/device")
    @Timed
    public List<SettingUser> getSettingsAndUsersByDeviceId(@RequestParam Long id) {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get Settings : {}", id);
        }
        return settingsService.getSettingAndUserNameByDeviceId(id);
    }

    @DeleteMapping("/settings")
    @Timed
    public ResponseEntity deleteSettings(@RequestParam Long id) {
        if (log.isInfoEnabled()) {
            log.info("REST request to delete Settings : {}", id);
        }
        if (settingsService.deleteSettings(id) > 0) {
            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME,
                    id.toString())).build();
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
