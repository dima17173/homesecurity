package com.impl.homesecurity.web.rest.errors;

import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AppException extends RuntimeException {

    ExceptionCodes exceptionCodes;

    public AppException(ExceptionCodes exceptionCodes,Throwable cause) {

        super(cause);
        this.exceptionCodes = exceptionCodes;
    }
}
