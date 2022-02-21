package com.chfreitas.sysbank.controller;

import java.util.List;

import com.chfreitas.sysbank.dto.AccHolderDTO;
import com.chfreitas.sysbank.exception.InvalidAccountException;
import com.chfreitas.sysbank.model.AccHolder;
import com.chfreitas.sysbank.service.AccHolderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for requests related to the Account Holder functionalities.
 */
@Controller
@RequestMapping("/accholder")
public class AccHolderController {

    // Service that encapsulates business rules for Account Holders
    @Autowired
    private AccHolderService accHolderService;

    /**
     * Loads the Account Holder's data, if exists.
     * 
     * @param model
     * @return
     */
    @GetMapping("")
    public String listAccHolders(Model model) {
        // Full search of account holders
        List<AccHolder> accHolders = accHolderService.findAll();

        // Adds the attribute only in case of existence
        if (accHolders != null && !accHolders.isEmpty()) {
            model.addAttribute("accHolders", accHolders);
        }

        // Redirects to the "accHolders" template
        return "accHolders";
    }

    /**
     * Adds a new Account Holder.
     * 
     * @param accHolderDTO
     * @param redirectAttributes
     * @return
     */
    @PostMapping("")
    public String addAccHolder(AccHolderDTO accHolderDTO, RedirectAttributes redirectAttributes) {
        try {
            // Only redirects to the service layer...
            accHolderService.addAccHolder(accHolderDTO);
        } catch (InvalidAccountException e) {
            // ... and catch proper errors, if exists
            e.printStackTrace();
            // Sends the error message as a temp attribute (that avoid URL param change)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        // Redirects to the "accHolders" GET request
        return "redirect:/accholder";
    }

}
