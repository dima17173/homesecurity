package com.impl.homesecurity.domain;

import lombok.*;

/**
 * Created by dima.
 * Creation date 28.11.18.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class MainUser {

    private Long id;
    private String name;
    private String login;
    private String deviceName;
}
