package com.chfreitas.sysbank.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import com.chfreitas.sysbank.exception.BusinessException;
import com.chfreitas.sysbank.model.Transfer;
import com.chfreitas.sysbank.service.TransferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for API requests related to the Transfer entity.
 */
@RestController
@RequestMapping("/transfer/api")
public class TransferAPIController {

    // Service that encapsulates business rules for Transfers
    @Autowired
    private TransferService transferService;

    /**
     * Searches for transfers based on a date tranfer period range.
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/search/date")
    public List<Transfer> findTransfersByDate(@PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate) {
        try {
            return transferService.findByDate(startDate, endDate);
        } catch (BusinessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
