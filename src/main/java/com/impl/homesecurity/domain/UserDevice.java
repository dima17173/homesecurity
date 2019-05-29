package com.impl.homesecurity.domain;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserDevice {

    private Long userId;
    private Long deviceId;
}
