package com.chfreitas.sysbank.exception;

/**
 * Exception that must be launched when some problem related with Account Holder
 * occurs.
 */
public class InvalidAccHolderException extends Exception {

    public InvalidAccHolderException(String message) {
        super(message);
    }

}
