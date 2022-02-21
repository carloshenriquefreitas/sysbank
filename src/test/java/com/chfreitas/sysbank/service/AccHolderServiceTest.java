package com.chfreitas.sysbank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.chfreitas.sysbank.dto.AccHolderDTO;
import com.chfreitas.sysbank.exception.InvalidAccountException;
import com.chfreitas.sysbank.model.AccHolder;
import com.chfreitas.sysbank.repository.AccHolderRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class AccHolderServiceTest {

    @Autowired
    private AccHolderService accHolderService;

    @MockBean
    private AccHolderRepository accHolderRepository;

    @Test
    void testIfCpfCnpjIsNullOrEmptyWhileQueryingAnAccHolder() {
        assertNull(accHolderService.findByCpf(null));
        assertNull(accHolderService.findByCpf(""));
    }

    @Test
    void testIfFindByCpfReturnsNullForWrongCpf() {
        String emptyCpf = "   ";
        Mockito.when(accHolderRepository.findByCpf(emptyCpf)).thenReturn(null);
        assertNull(accHolderService.findByCpf(emptyCpf));

        String validCpf = "31292455020";
        Mockito.when(accHolderRepository.findByCpf(validCpf)).thenReturn(null);
        assertNull(accHolderService.findByCpf(validCpf));
    }

    @Test
    void testIfFindByCpfReturnsValidAccHolderForCorrectgCpf() {
        String validCpf = "31292455020";
        AccHolder mockAccHolder = new AccHolder();
        mockAccHolder.setId(1L);
        mockAccHolder.setCpf(validCpf);
        mockAccHolder.setName("Fake Acc Holder");

        Mockito.when(accHolderRepository.findByCpf(validCpf)).thenReturn(mockAccHolder);
        assertEquals(mockAccHolder, accHolderService.findByCpf(validCpf));
    }

    @Test
    void testIfAddAccHolderThrowsAnInvalidAccountExceptionForInvalidParameters() {
        final AccHolderDTO accHolderDTONull = null;
        assertThrows(InvalidAccountException.class, () -> {
            accHolderService.addAccHolder(accHolderDTONull);
        });

        final AccHolderDTO accHolderDTOEmpty = new AccHolderDTO();
        assertThrows(InvalidAccountException.class, () -> {
            accHolderService.addAccHolder(accHolderDTOEmpty);
        });

        final AccHolderDTO accHolderDTOEmptyName = new AccHolderDTO();
        accHolderDTOEmptyName.setName("");
        assertThrows(InvalidAccountException.class, () -> {
            accHolderService.addAccHolder(accHolderDTOEmptyName);
        });

        final AccHolderDTO accHolderDTOEmptyCpf = new AccHolderDTO();
        accHolderDTOEmptyCpf.setName("Fake Acc Holder");
        accHolderDTOEmptyCpf.setCpf("");
        assertThrows(InvalidAccountException.class, () -> {
            accHolderService.addAccHolder(accHolderDTOEmptyName);
        });
    }

}
