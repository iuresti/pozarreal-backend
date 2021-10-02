package org.uresti.pozarreal.controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.uresti.pozarreal.dto.ErrorResponse;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.exception.MaintenanceFeeOverPassedException;
import org.uresti.pozarreal.exception.PozarrealSystemException;

import java.util.Locale;

@ControllerAdvice
public class PozarrealExceptionHandler {

    private final MessageSource messageSource;

    public PozarrealExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({PozarrealSystemException.class})
    public ResponseEntity<ErrorResponse> handleBadInputException(PozarrealSystemException ex, Locale locale) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code(ex.getCode())
                .exceptionName(ex.getClass().getCanonicalName())
                .message(messageSource.getMessage(ex.getCode(), ex.getArgs(), locale))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BadRequestDataException.class})
    public ResponseEntity<ErrorResponse> handleBadInputException(BadRequestDataException ex, Locale locale) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code(ex.getCode())
                .exceptionName(ex.getClass().getCanonicalName())
                .message(messageSource.getMessage(ex.getCode(), ex.getArgs(), locale))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MaintenanceFeeOverPassedException.class})
    public ResponseEntity<ErrorResponse> handleBadInputException(MaintenanceFeeOverPassedException ex, Locale locale) {
        return new ResponseEntity<>(ErrorResponse.builder()
                .code(ex.getCode())
                .exceptionName(ex.getClass().getCanonicalName())
                .message(messageSource.getMessage(ex.getCode(), ex.getArgs(), locale))
                .build(), HttpStatus.NOT_ACCEPTABLE);
    }


}
