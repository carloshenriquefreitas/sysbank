package com.chfreitas.sysbank.exception;

/**
 * Exception used to encapsulate all the exceptions occurred on the system.
 * This way, dealing with exception handler on controller layer becomes easier.
 */
public class BusinessException extends Exception {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable error) {
        super(error);
    }

    public BusinessException(String message, Throwable error) {
        super(message, error);
    }

}
