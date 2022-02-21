package com.chfreitas.sysbank.model;

/**
 * Enum that represents the domain values for the type of a Transfer.
 */
public enum TransferType {

    DEPOSIT(1),
    WITHDRAW(2),
    TRANSFER(3);

    private Integer code;

    private TransferType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
