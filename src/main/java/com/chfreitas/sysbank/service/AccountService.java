package com.chfreitas.sysbank.service;

import java.math.BigDecimal;
import java.util.List;

import com.chfreitas.sysbank.dto.AccountDTO;
import com.chfreitas.sysbank.exception.InvalidAccHolderException;
import com.chfreitas.sysbank.exception.InvalidAccountException;
import com.chfreitas.sysbank.exception.InvalidOperationException;
import com.chfreitas.sysbank.model.AccHolder;
import com.chfreitas.sysbank.model.Account;
import com.chfreitas.sysbank.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Encapsulates the business rules related to the Account entity.
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccHolderService accHolderService;

    /**
     * Returns all the Accounts registered on the system.
     * 
     * @return
     */
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    /**
     * Searches for an Account by its ID.
     * 
     * @param id
     * @return
     */
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    /**
     * Adds an Account to the system.
     * 
     * @param accountDTO
     * @throws InvalidAccountException
     * @throws InvalidAccHolderException
     */
    public void addAccount(AccountDTO accountDTO) throws InvalidAccountException, InvalidAccHolderException {
        // Verify if the required parameters are valid
        boolean isValid = accountDTO != null && accountDTO.getPrincipalCPF() != null
                && !accountDTO.getPrincipalCPF().isEmpty();

        // Throws and InvalidAccountException in case of invalid parameters
        if (!isValid) {
            throw new InvalidAccountException("Account parameters are not valid.");
        }

        // Searches for and Account Holder by its CPF
        AccHolder accHolder = accHolderService.findByCpf(accountDTO.getPrincipalCPF());

        // Throws an InvalidAccHolderException in case of no Account Holders found
        if (accHolder == null) {
            throw new InvalidAccHolderException("Account holder not found.");
        }

        // Creates an Account entity and fills with the DTO values
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setDescription(accountDTO.getDescription());
        account.setAccHolder(accHolder);
        addAccount(account);
    }

    /**
     * Adds an Account to the system.
     * 
     * @param account
     */
    public void addAccount(Account account) {
        accountRepository.save(account);
    }

    /**
     * Increments the balance of an Account by a determined amount.
     * 
     * @param account
     * @param amount
     * @return
     */
    public Account incrementBalance(Account account, BigDecimal amount) {
        BigDecimal balance = account.getBalance().add(amount);
        account.setBalance(balance);

        return accountRepository.save(account);
    }

    /**
     * Decrements the balance of an Account by a determined amount.
     * 
     * @param account
     * @param amount
     * @return
     * @throws InvalidOperationException
     */
    public Account decrementBalance(Account account, BigDecimal amount) throws InvalidOperationException {
        BigDecimal balance = account.getBalance();

        // Throws an InvalidOperationException in case of insufficient balance
        if (balance.compareTo(amount) < 0) {
            throw new InvalidOperationException("Insufficient balance for this operation.");
        }

        balance = balance.subtract(amount);
        account.setBalance(balance);

        return accountRepository.save(account);
    }

}
