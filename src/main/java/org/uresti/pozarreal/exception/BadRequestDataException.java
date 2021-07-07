package org.uresti.pozarreal.exception;

public class BadRequestDataException extends PozarrealSystemException {

    public BadRequestDataException(String message, String code, Object... args) {
        super(message, code, args);
    }
}
