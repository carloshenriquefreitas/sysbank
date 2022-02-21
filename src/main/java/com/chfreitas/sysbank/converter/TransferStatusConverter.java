package com.chfreitas.sysbank.converter;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

import com.chfreitas.sysbank.model.TransferStatus;

/**
 * Converter responsible to the back and forth value convertion related to the
 * TransferStatus enum type and database registration value.
 */
public class TransferStatusConverter implements AttributeConverter<TransferStatus, Integer> {

    /**
     * Gets the enum value and returns the code used on database.
     */
    @Override
    public Integer convertToDatabaseColumn(TransferStatus status) {
        if (status == null) {
            return null;
        }

        return status.getCode();
    }

    /**
     * Gets the value on the database and return the enum correct value.
     */
    @Override
    public TransferStatus convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        // Walks through the enum values to find the correct match
        return Stream.of(TransferStatus.values())
                .filter(s -> s.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
