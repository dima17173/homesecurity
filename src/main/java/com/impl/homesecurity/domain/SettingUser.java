package com.impl.homesecurity.domain;

import lombok.*;

import java.util.List;

/**
 * Created by pavel on 21.01.19.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SettingUser {
    private Long id;
    private String value;
    private String userName;
    private Long userId;
}
