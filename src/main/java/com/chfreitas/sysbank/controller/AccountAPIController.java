package com.chfreitas.sysbank.controller;

import com.chfreitas.sysbank.model.Account;
import com.chfreitas.sysbank.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for API requests related to the Account entity.
 */
@RestController
@RequestMapping("/account/api")
public class AccountAPIController {

    // Service that encapsulates business rules for Accounts
    @Autowired
    private AccountService accountService;

    /**
     * Searches for an Account based on its ID.
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Account findById(@PathVariable("id") Long id) {
        Account account = accountService.findById(id);

        if (account != null) {
            return account;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no Account for the specified ID.");
        }
    }

}
