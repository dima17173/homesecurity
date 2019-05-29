package com.impl.homesecurity.web.rest.errors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HttpClientResponseError {

    private String error;
    private String errorCode;
}
