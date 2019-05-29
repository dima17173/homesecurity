package com.impl.homesecurity.domain;

import lombok.*;

/**
 * Created by diana.
 * Creation date 20.11.18.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SmsSender {

    private String phone;
    private String message;
}
