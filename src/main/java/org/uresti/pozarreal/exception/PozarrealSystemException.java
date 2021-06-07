package org.uresti.pozarreal.exception;

import java.sql.SQLException;

public class PozarrealSystemException extends RuntimeException {
    public PozarrealSystemException(String message, SQLException cause) {
        super(message, cause);
    }

    public PozarrealSystemException(String message) {
        super(message);
    }
}
