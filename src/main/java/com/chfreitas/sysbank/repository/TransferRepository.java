package com.chfreitas.sysbank.repository;

import java.util.Date;
import java.util.List;

import com.chfreitas.sysbank.model.Transfer;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Encapulates the rules for accessing a data repository for the Transfer
 * entity.
 */
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    public List<Transfer> findBydateBetween(Date startDate, Date endDate);

}
