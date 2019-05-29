package com.impl.homesecurity.service.dto;

import lombok.*;

/**
 * Created by dima.
 * Creation date 14.01.19.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SettingsSaveDTO {
    private Long id;
    private Long userId;
    private Long deviceId;
    private AutoModeSaveDTO value;

}
