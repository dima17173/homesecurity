package com.impl.homesecurity.web.rest.errors;

import com.impl.homesecurity.service.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by diana.
 * Creation date 27.11.18.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleBadRequestException(BadRequestException exception) {
        Throwable cause = exception.getCause();
        log.info(String.valueOf(cause));
        return new ErrorDTO(exception.getClass().getSimpleName(), exception.getMessage());
    }

    @ExceptionHandler(BadRequestAlertException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleBadRequestAlertException(BadRequestAlertException exception) {
        Throwable cause = exception.getCause();
        log.info(String.valueOf(cause));
        return new ErrorDTO(exception.getClass().getSimpleName(), exception.getMessage(), String.valueOf(cause));
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleAppException(AppException exception) {
        Throwable cause = exception.getCause();
        log.info(String.valueOf(cause));
        return new ErrorDTO(String.valueOf(exception.exceptionCodes.getId()), exception.exceptionCodes.getMsg(), String.valueOf(cause));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleResourceNotFoundException(ResourceNotFoundException exception) {
        Throwable cause = exception.getCause();
        log.info(String.valueOf(cause));
        return new ErrorDTO(exception.getClass().getSimpleName(), exception.getMessage(), String.valueOf(cause));
    }
}
