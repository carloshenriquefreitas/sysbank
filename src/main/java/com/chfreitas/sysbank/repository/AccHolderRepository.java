package com.chfreitas.sysbank.repository;

import com.chfreitas.sysbank.model.AccHolder;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Encapulates the rules for accessing a data repository for the AccHolder
 * entity.
 */
public interface AccHolderRepository extends JpaRepository<AccHolder, Long> {

    /**
     * Executes a search on the Account Holder's data based on the CPF parameter.
     * 
     * @param cpf
     * @return
     */
    AccHolder findByCpf(String cpf);

}
