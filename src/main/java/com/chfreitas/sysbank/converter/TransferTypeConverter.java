package com.chfreitas.sysbank.converter;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

import com.chfreitas.sysbank.model.TransferType;

/**
 * Converter responsible to the back and forth value convertion related to the
 * TransferType enum type and database registration value.
 */
public class TransferTypeConverter implements AttributeConverter<TransferType, Integer> {

    /**
     * Gets the enum value and returns the code used on database.
     */
    @Override
    public Integer convertToDatabaseColumn(TransferType type) {
        if (type == null) {
            return null;
        }

        return type.getCode();
    }

    /**
     * Gets the value on the database and return the enum correct value.
     */
    @Override
    public TransferType convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        // Walks through the enum values to find the correct match
        return Stream.of(TransferType.values())
                .filter(t -> t.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
