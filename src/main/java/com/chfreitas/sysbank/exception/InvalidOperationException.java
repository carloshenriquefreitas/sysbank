package com.chfreitas.sysbank.exception;

/**
 * Exception that must be launched when some problem related with the execution
 * of an operation occurs.
 */
public class InvalidOperationException extends Exception {

    public InvalidOperationException(String message) {
        super(message);
    }

}
