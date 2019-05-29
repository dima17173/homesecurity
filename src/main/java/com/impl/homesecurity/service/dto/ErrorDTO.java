package com.impl.homesecurity.service.dto;

import lombok.*;

/**
 * Created by diana.
 * Creation date 27.11.18.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ErrorDTO {

    private String exceptionName;
    private String message;
    private String cause;

    public ErrorDTO(String exceptionName, String message) {
        this.exceptionName = exceptionName;
        this.message = message;
    }
}
