package com.chfreitas.sysbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import com.chfreitas.sysbank.dto.AccountDTO;
import com.chfreitas.sysbank.exception.InvalidAccHolderException;
import com.chfreitas.sysbank.exception.InvalidAccountException;
import com.chfreitas.sysbank.exception.InvalidOperationException;
import com.chfreitas.sysbank.model.Account;
import com.chfreitas.sysbank.repository.AccountRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccHolderService accHolderService;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    void testIfAddAccountThrowsAnInvalidAccountExceptionForInvalidParameters() {
        final AccountDTO accountDTONull = null;
        assertThrows(InvalidAccountException.class, () -> {
            accountService.addAccount(accountDTONull);
        });

        final AccountDTO accountDTOEmpty = new AccountDTO();
        assertThrows(InvalidAccountException.class, () -> {
            accountService.addAccount(accountDTOEmpty);
        });

        final AccountDTO accountDTOEmptyCpf = new AccountDTO();
        accountDTOEmptyCpf.setPrincipalCPF("");
        assertThrows(InvalidAccountException.class, () -> {
            accountService.addAccount(accountDTOEmptyCpf);
        });
    }

    @Test
    void testIfAddAccountThrowsInvalidAccHolderExceptionForWrongAccHolderCPF() {
        String validCpf = "31292455020";
        final AccountDTO accountDTOEmptyCpf = new AccountDTO();
        accountDTOEmptyCpf.setPrincipalCPF(validCpf);

        Mockito.when(accHolderService.findByCpf(validCpf)).thenReturn(null);

        assertThrows(InvalidAccHolderException.class, () -> {
            accountService.addAccount(accountDTOEmptyCpf);
        });
    }

    @Test
    void testIfIncrementBalanceUpdatesTheRelatedAccountCorrectly() {
        BigDecimal amount = BigDecimal.TEN;

        Account account = new Account();
        account.setBalance(BigDecimal.ONE);

        Account accountMock = new Account();
        accountMock.setBalance(new BigDecimal(11));

        Mockito.when(accountRepository.save(account)).thenReturn(accountMock);
        assertEquals(accountMock, accountService.incrementBalance(account, amount));
    }

    @Test
    void testIfDecrementBalanceThrowsInvalidOperationExceptionInCaseOfInsufficientBalance() {
        final BigDecimal amount = BigDecimal.TEN;
        final Account account = new Account();
        account.setBalance(BigDecimal.ONE);

        assertThrows(InvalidOperationException.class, () -> {
            accountService.decrementBalance(account, amount);
        });
    }

    @Test
    void testIfDecrementBalanceUpdatesTheRelatedAccountCorrectly() {
        BigDecimal amount = BigDecimal.ONE;

        Account account = new Account();
        account.setBalance(BigDecimal.TEN);

        Account accountMock = new Account();
        accountMock.setBalance(new BigDecimal(9));

        Mockito.when(accountRepository.save(account)).thenReturn(accountMock);

        try {
            assertEquals(accountMock, accountService.decrementBalance(account, amount));
        } catch (InvalidOperationException e) {
            e.printStackTrace();
        }
    }

}
