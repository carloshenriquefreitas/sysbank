package com.chfreitas.sysbank.controller;

import java.util.List;

import com.chfreitas.sysbank.dto.AccountDTO;
import com.chfreitas.sysbank.exception.InvalidAccHolderException;
import com.chfreitas.sysbank.exception.InvalidAccountException;
import com.chfreitas.sysbank.model.Account;
import com.chfreitas.sysbank.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for requests related to the Account functionalities.
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    // Service that encapsulates business rules for Account
    @Autowired
    private AccountService accountService;

    /**
     * Loads the Account's data, if exists.
     * 
     * @param model
     * @return
     */
    @GetMapping("")
    public String listAccountsByCpf(Model model) {
        // Full search of accounts
        List<Account> accounts = accountService.findAll();

        // Adds the attribute only in case of existence
        if (accounts != null && !accounts.isEmpty()) {
            model.addAttribute("accounts", accounts);
        }

        // Redirects to the "accounts" template
        return "accounts";
    }

    /**
     * Adds a new Account.
     * @param accountDTO
     * @param redirectAttributes
     * @return
     */
    @PostMapping("")
    public String addAccount(AccountDTO accountDTO, RedirectAttributes redirectAttributes) {
        try {
            // Only redirects to the service layer...
            accountService.addAccount(accountDTO);
        } catch (InvalidAccountException | InvalidAccHolderException e) {
            // ... and catch proper errors, if exists
            e.printStackTrace();
            // Sends the error message as a temp attribute (that avoid URL param change)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        // Redirects to the "accounts" GET request
        return "redirect:/account";
    }

}
