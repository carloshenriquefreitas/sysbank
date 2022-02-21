package com.chfreitas.sysbank.controller;

import com.chfreitas.sysbank.model.AccHolder;
import com.chfreitas.sysbank.service.AccHolderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for API requests related to the Account Holder entity.
 */
@RestController
@RequestMapping("/accholder/api")
public class AccHolderAPIController {

    // Service that encapsulates business rules for Account Holders
    @Autowired
    private AccHolderService accHolderService;

    /**
     * Searches for an Account Holder based on its CPF.
     * 
     * @param cpf
     * @return
     */
    @GetMapping("/{cpf}")
    public AccHolder findAccHolderByCpf(@PathVariable("cpf") String cpf) {
        AccHolder accHolder = accHolderService.findByCpf(cpf);

        if (accHolder != null) {
            return accHolder;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There's no Account Holder for the specified CPF.");
        }
    }

}
