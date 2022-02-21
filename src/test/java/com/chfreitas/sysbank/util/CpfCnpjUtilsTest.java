package com.chfreitas.sysbank.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CpfCnpjUtilsTest {

    @Test
    void testIfCpfIsValid() {
        String validCpf = "31292455020";
        assertTrue(CpfCnpjUtils.isValid(validCpf));
    }

    @Test
    void testIfCpfIsVInvalid() {
        String invalidCpf = "105643296570";
        assertFalse(CpfCnpjUtils.isValid(invalidCpf));
    }

    @Test
    void testIfCnpjIsValid() {
        String validCnpj = "93180406000184";
        assertTrue(CpfCnpjUtils.isValid(validCnpj));
    }

    @Test
    void testIfCnpjIsInvalid() {
        String invalidCnpj = "10547328690546";
        assertFalse(CpfCnpjUtils.isValid(invalidCnpj));
    }

}
