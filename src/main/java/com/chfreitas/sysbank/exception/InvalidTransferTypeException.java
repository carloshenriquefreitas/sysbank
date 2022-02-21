package com.chfreitas.sysbank.exception;

/**
 * Exception that must be launched when some problem related with Tranfer
 * occurs.
 */
public class InvalidTransferTypeException extends Exception {

    public InvalidTransferTypeException(String message) {
        super(message);
    }

}
