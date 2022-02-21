package com.chfreitas.sysbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.chfreitas.sysbank.dto.TransferDTO;
import com.chfreitas.sysbank.exception.InvalidAccountException;
import com.chfreitas.sysbank.exception.InvalidOperationException;
import com.chfreitas.sysbank.model.Account;
import com.chfreitas.sysbank.model.TransferType;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @MockBean
    private AccountService accountService;

    @Test
    void testIfCreateTransferInstanceThrowsANumberFormatExceptionForAnInvalidAccountId() {
        TransferDTO transferDTO = new TransferDTO();
        String wrongAccountId = "abc";

        assertThrows(NumberFormatException.class, () -> {
            transferService.createTransferInstance(transferDTO, wrongAccountId, TransferType.DEPOSIT);
        });
    }

    @Test
    void testIfCreateTransferInstanceThrowsAnInvalidAccountExceptionForAnInvalidAccountId() {
        TransferDTO transferDTO = new TransferDTO();
        Long wrongAccountId = 123L;

        Mockito.when(accountService.findById(wrongAccountId)).thenReturn(null);
        assertThrows(InvalidAccountException.class, () -> {
            transferService.createTransferInstance(transferDTO, wrongAccountId.toString(), TransferType.DEPOSIT);
        });
    }

    @Test
    void testIfCreateTransferInstanceThrowsAParseExceptionForAnInvalidTransferDate() {
        final Long fakeAccountId = 123L;

        final TransferDTO transferDTO = new TransferDTO();
        transferDTO.setDate("abc");

        Mockito.when(accountService.findById(fakeAccountId)).thenReturn(new Account());
        assertThrows(ParseException.class, () -> {
            transferService.createTransferInstance(transferDTO, fakeAccountId.toString(), TransferType.DEPOSIT);
        });

        transferDTO.setDate("18/02/2022");
        assertThrows(ParseException.class, () -> {
            transferService.createTransferInstance(transferDTO, fakeAccountId.toString(), TransferType.DEPOSIT);
        });

        transferDTO.setDate("02/18/2022");
        assertThrows(ParseException.class, () -> {
            transferService.createTransferInstance(transferDTO, fakeAccountId.toString(), TransferType.DEPOSIT);
        });

        transferDTO.setDate("2022/18/22");
        assertThrows(ParseException.class, () -> {
            transferService.createTransferInstance(transferDTO, fakeAccountId.toString(), TransferType.DEPOSIT);
        });
    }

    @Test
    void testIfCreateTransferInstanceThrowsANumberFormatExceptionForAnInvalidAmount() {
        final Long fakeAccountId = 123L;

        final TransferDTO transferDTO = new TransferDTO();
        transferDTO.setDate("2022-02-19");
        transferDTO.setAmount("abc");

        Mockito.when(accountService.findById(fakeAccountId)).thenReturn(new Account());
        assertThrows(NumberFormatException.class, () -> {
            transferService.createTransferInstance(transferDTO, fakeAccountId.toString(), TransferType.DEPOSIT);
        });
    }

    @Test
    void testIfGetInstallmentsValuesThrowsANumberFormatExceptionForInvalidStallments() {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setInstallments("abc");

        assertThrows(NumberFormatException.class, () -> {
            transferService.getInstallmentsValues(transferDTO);
        });

        transferDTO.setInstallments("");
        assertThrows(NumberFormatException.class, () -> {
            transferService.getInstallmentsValues(transferDTO);
        });

        transferDTO.setInstallments("   ");
        assertThrows(NumberFormatException.class, () -> {
            transferService.getInstallmentsValues(transferDTO);
        });
    }

    @Test
    void testIfGetInstallmentsValuesThrowsANumberFormatExceptionForInvalidTransferAmount() {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setInstallments("5");
        transferDTO.setAmount("abc");

        assertThrows(NumberFormatException.class, () -> {
            transferService.getInstallmentsValues(transferDTO);
        });

        transferDTO.setAmount("");
        assertThrows(NumberFormatException.class, () -> {
            transferService.getInstallmentsValues(transferDTO);
        });

        transferDTO.setAmount("   ");
        assertThrows(NumberFormatException.class, () -> {
            transferService.getInstallmentsValues(transferDTO);
        });
    }

    void testIfGetInstallmentsValuesThrowsAnInvalidOperationExceptionForZeroInstallments() {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setInstallments("0");
        transferDTO.setAmount("100");

        assertThrows(InvalidOperationException.class, () -> {
            transferService.getInstallmentsValues(transferDTO);
        });
    }

    @Test
    void testIfInstallmentValuesWasCreatedCorrectly() {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setInstallments("2");
        transferDTO.setAmount("20");

        List<BigDecimal> resultExpected = new ArrayList<>();
        resultExpected.add(BigDecimal.TEN);
        resultExpected.add(BigDecimal.TEN);

        try {
            assertEquals(resultExpected, transferService.getInstallmentsValues(transferDTO));
        } catch (NumberFormatException | InvalidOperationException e) {
            e.printStackTrace();
        }

        transferDTO.setInstallments("3");
        transferDTO.setAmount("20");

        resultExpected.clear();
        resultExpected.add(new BigDecimal(8));
        resultExpected.add(new BigDecimal(6));
        resultExpected.add(new BigDecimal(6));

        try {
            assertEquals(resultExpected, transferService.getInstallmentsValues(transferDTO));
        } catch (NumberFormatException | InvalidOperationException e) {
            e.printStackTrace();
        }
    }

}
