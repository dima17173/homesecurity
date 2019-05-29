package com.impl.homesecurity.domain;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Device {

    private Long id;
    private String deviceNumber;
    private String name;
    private boolean status;
    private String hexName;
}
