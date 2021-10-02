package org.uresti.pozarreal.exception;

public class MaintenanceFeeOverPassedException extends PozarrealSystemException{

    public MaintenanceFeeOverPassedException(String message, String code, Object... args) {
        super(message, code, args);
    }
}
