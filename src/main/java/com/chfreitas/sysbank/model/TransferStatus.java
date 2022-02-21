package com.chfreitas.sysbank.model;

/**
 * Enum that represents the domain values for the status of a Transfer.
 */
public enum TransferStatus {

    SCHEDULED(1),
    COMPLETED(2),
    CANCELED(3);

    private Integer code;

    private TransferStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
