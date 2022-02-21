package com.chfreitas.sysbank.controller;

import java.util.List;

import com.chfreitas.sysbank.dto.TransferDTO;
import com.chfreitas.sysbank.exception.BusinessException;
import com.chfreitas.sysbank.model.Transfer;
import com.chfreitas.sysbank.service.TransferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for requests related to the Transfer functionalities.
 */
@Controller
@RequestMapping("/transfer")
public class TransferController {

    // Service that encapsulates business rules for Transfer
    @Autowired
    private TransferService transferService;

    /**
     * Loads the Transfer data, if exists.
     * @param model
     * @return
     */
    @GetMapping("")
    public String listTransfers(Model model) {
        // Full search of transfers
        List<Transfer> transfers = transferService.findAll();

        // Adds the attribute only in case of existence
        if (transfers != null && !transfers.isEmpty()) {
            model.addAttribute("transfers", transfers);
        }

        // Redirects to the "transfers" template
        return "transfers";
    }

    /**
     * Executes a transfer no matter what type it has.
     * @param transferDTO
     * @param redirectAttributes
     * @return
     */
    @PostMapping("")
    public String executeTransfer(TransferDTO transferDTO, RedirectAttributes redirectAttributes) {
        try {
            // Only redirects to the service layer...
            transferService.executeTransfer(transferDTO);
        } catch (BusinessException e) {
            // ... and catch proper errors, if exists
            e.printStackTrace();
            // Sends the error message as a temp attribute (that avoid URL param change)
            redirectAttributes.addFlashAttribute("errorMessage", e.getCause().getMessage());
        }
        
        // Redirects to the "transfers" GET request
        return "redirect:/transfer";
    }

    /**
     * Cancels a transfer and any other related transfer.
     * @param transferId
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/{transferId}/cancel")
    public String cancelTransfer(@PathVariable("transferId") Long transferId, RedirectAttributes redirectAttributes) {
        try {
            // Only redirects to the service layer...
            transferService.cancelTransfer(transferId);
        } catch (BusinessException e) {
            // ... and catch proper errors, if exists
            e.printStackTrace();
            // Sends the error message as a temp attribute (that avoid URL param change)
            redirectAttributes.addFlashAttribute("errorMessage", e.getCause().getMessage());
        }

        // Redirects to the "transfers" GET request
        return "redirect:/transfer";
    }

}
