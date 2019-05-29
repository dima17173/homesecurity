package com.impl.homesecurity.payload;

import com.impl.homesecurity.domain.enumeration.ExceptionCodes;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
@ToString
public class ApiExceptionResponse  extends Exception {

    private int errorCode;
    private String errorMsg;

    public ApiExceptionResponse(ExceptionCodes code) {
        this.errorMsg = code.getMsg();
        this.errorCode = code.getId();
    }
}
