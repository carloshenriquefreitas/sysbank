package com.chfreitas.sysbank.service;

import java.util.List;

import com.chfreitas.sysbank.dto.AccHolderDTO;
import com.chfreitas.sysbank.exception.InvalidAccountException;
import com.chfreitas.sysbank.model.AccHolder;
import com.chfreitas.sysbank.repository.AccHolderRepository;
import com.chfreitas.sysbank.util.CpfCnpjUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Encapsulates the business rules related to the Account Holder entity.
 */
@Service
public class AccHolderService {

    @Autowired
    private AccHolderRepository accHolderRepository;

    /**
     * Returns all the Account Holders registered on the system.
     * 
     * @return
     */
    public List<AccHolder> findAll() {
        return accHolderRepository.findAll();
    }

    /**
     * Adds an Account Holder to the system.
     * 
     * @param accHolderDTO
     * @throws InvalidAccountException
     */
    public void addAccHolder(AccHolderDTO accHolderDTO) throws InvalidAccountException {
        // Verify if the required parameters are valid
        boolean isValid = accHolderDTO != null
                && accHolderDTO.getName() != null && !accHolderDTO.getName().isEmpty()
                && accHolderDTO.getCpf() != null && !accHolderDTO.getCpf().isEmpty();

        // Throws and InvalidAccountException in case of invalid parameters
        if (!isValid || !CpfCnpjUtils.isValid(accHolderDTO.getCpf())) {
            throw new InvalidAccountException("Invalid account parameters.");
        }

        // Creates an Account Holder entity and fills with the DTO values
        AccHolder accHolder = new AccHolder();
        accHolder.setName(accHolderDTO.getName());
        accHolder.setCpf(accHolderDTO.getCpf());
        accHolderRepository.save(accHolder);
    }

    /**
     * Searches for an Account Holder by its CPF.
     * 
     * @param cpf
     * @return
     */
    public AccHolder findByCpf(String cpf) {
        AccHolder accHolder = null;

        // Executes the search only if the parameter exists
        if (cpf != null && !cpf.isEmpty()) {
            accHolder = accHolderRepository.findByCpf(cpf);
        }

        return accHolder;
    }

}
