package com.impl.homesecurity.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ClientMessage {

    private long id;
    private String received;
    private String phone;
    private String message;
    private String toPhone;
    private String sent;
}
