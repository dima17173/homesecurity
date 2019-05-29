package com.impl.homesecurity.domain;

import lombok.*;

/**
 * Created by dima.
 * Creation date 23.10.18.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Settings {

    private Long id;
    private Long userId;
    private Long deviceId;
    private String value;
}
