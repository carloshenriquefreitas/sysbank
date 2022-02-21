package com.chfreitas.sysbank.exception;

/**
 * Exception that must be launched when some problem related with Account
 * occurs.
 */
public class InvalidAccountException extends Exception {

    public InvalidAccountException(String message) {
        super(message);
    }

}
