package com.chfreitas.sysbank.repository;

import com.chfreitas.sysbank.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Encapulates the rules for accessing a data repository for the Account
 * entity.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

}
