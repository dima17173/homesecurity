package com.impl.homesecurity.domain;

import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DeviceUser {

    private Long id;
    private String deviceNumber;
    private String name;
    private boolean status;
    private String hexName;
    private List<User> users;
}
