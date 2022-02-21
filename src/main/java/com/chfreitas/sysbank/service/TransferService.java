package com.chfreitas.sysbank.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.chfreitas.sysbank.dto.TransferDTO;
import com.chfreitas.sysbank.exception.BusinessException;
import com.chfreitas.sysbank.exception.InvalidAccountException;
import com.chfreitas.sysbank.exception.InvalidOperationException;
import com.chfreitas.sysbank.exception.InvalidTransferTypeException;
import com.chfreitas.sysbank.model.Account;
import com.chfreitas.sysbank.model.Transfer;
import com.chfreitas.sysbank.model.TransferStatus;
import com.chfreitas.sysbank.model.TransferType;
import com.chfreitas.sysbank.repository.TransferRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Encapsulates the business rules related to the Transfer entity.
 */
@Service
public class TransferService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransferRepository transferRepository;

    /**
     * Executes a transfer on the system.
     * 
     * @param transferDTO
     * @throws BusinessException
     */
    public void executeTransfer(TransferDTO transferDTO)
            throws BusinessException {
        try {
            // Verify if the required parameters are valid
            boolean isValid = transferDTO != null
                    && transferDTO.getType() != null && !transferDTO.getType().isEmpty()
                    && transferDTO.getDate() != null && !transferDTO.getDate().isEmpty()
                    && transferDTO.getAmount() != null && !transferDTO.getAmount().isEmpty()
                    && transferDTO.getAccOrigin() != null && !transferDTO.getAccOrigin().isEmpty();

            if (isValid) {
                TransferType type = Stream.of(TransferType.values())
                        .filter(t -> t.getCode().toString().equals(transferDTO.getType()))
                        .findFirst().orElse(null);

                // Throws and InvalidTransferTpeException in case of null type
                if (type == null) {
                    throw new InvalidTransferTypeException("Transfer type not found.");
                }

                // Executes the transfer according to its type
                if (type.equals(TransferType.TRANSFER)) {
                    executeTransferBetweenAccounts(transferDTO);
                } else if (type.equals(TransferType.DEPOSIT)) {
                    executeDeposit(transferDTO, type);
                } else if (type.equals(TransferType.WITHDRAW)) {
                    executeWithdraw(transferDTO, type);
                } else {
                    // Throws an InvalidOperationException in case of undefined type
                    throw new InvalidOperationException("Invalid operation type.");
                }
            } else {
                // Throws and InvalidTransferTpeException in case of invalid parameters
                throw new InvalidTransferTypeException("Invalid transfer parameters.");
            }
        } catch (InvalidTransferTypeException | InvalidOperationException | ParseException | InvalidAccountException
                | NumberFormatException e) {
            // Encapsulates any type of exception occurred within a BusinessException
            throw new BusinessException(e);
        }
    }

    /**
     * Creates a Transfer entity based on its DTO related type, account ID and type
     * of transfer.
     * 
     * @param transferDTO
     * @param accountId
     * @param type
     * @return
     * @throws InvalidAccountException
     * @throws ParseException
     * @throws NumberFormatException
     */
    Transfer createTransferInstance(TransferDTO transferDTO, String accountId, TransferType type)
            throws InvalidAccountException, ParseException, NumberFormatException {
        // Search for and Account based on its ID
        Long accId = Long.parseLong(accountId);
        Account acc = accountService.findById(accId);

        if (acc == null) {
            throw new InvalidAccountException("Origin account not found.");
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date transferDate = format.parse(transferDTO.getDate());
        Date today = format.parse(format.format(new Date()));
        BigDecimal amount = new BigDecimal(transferDTO.getAmount());

        Transfer transfer = new Transfer();
        transfer.setType(type);
        transfer.setDate(transferDate);
        transfer.setAmount(amount);
        // Sets the Transfer status based on its date
        if (transferDate.equals(today)) {
            transfer.setStatus(TransferStatus.COMPLETED);
        } else {
            transfer.setStatus(TransferStatus.SCHEDULED);
        }
        transfer.setAccount(acc);

        return transfer;
    }

    /**
     * Executes a transfer of type deposit.
     * 
     * @param transferDTO
     * @param type
     * @return
     * @throws InvalidAccountException
     * @throws ParseException
     * @throws NumberFormatException
     */
    private Transfer executeDeposit(TransferDTO transferDTO, TransferType type)
            throws InvalidAccountException, ParseException, NumberFormatException {
        // Creates a Transfer instance
        Transfer transfer = createTransferInstance(transferDTO, transferDTO.getAccOrigin(), type);

        // Scheduled tranfers cannot update the account balance
        if (transfer.getStatus().equals(TransferStatus.COMPLETED)) {
            accountService.incrementBalance(transfer.getAccount(), transfer.getAmount());
        }

        return transferRepository.save(transfer);
    }

    /**
     * Executes a transfer of type withdraw.
     * 
     * @param transferDTO
     * @param type
     * @throws InvalidAccountException
     * @throws ParseException
     * @throws InvalidOperationException
     * @throws NumberFormatException
     */
    private void executeWithdraw(TransferDTO transferDTO, TransferType type)
            throws InvalidAccountException, ParseException, InvalidOperationException, NumberFormatException {
        // Creates a Transfer instance
        Transfer transfer = createTransferInstance(transferDTO, transferDTO.getAccOrigin(), type);

        // Scheduled tranfers cannot update the account balance
        if (transfer.getStatus().equals(TransferStatus.COMPLETED)) {
            accountService.decrementBalance(transfer.getAccount(), transfer.getAmount());
        }

        transferRepository.save(transfer);
    }

    /**
     * Executes a transfer tha withdraw an amount from one account and deposit on
     * another account.
     * 
     * @param transferDTO
     * @throws InvalidAccountException
     * @throws ParseException
     * @throws InvalidOperationException
     * @throws NumberFormatException
     */
    private void executeTransferBetweenAccounts(TransferDTO transferDTO)
            throws InvalidAccountException, ParseException, InvalidOperationException, NumberFormatException {
        // Gets the installment values for the transfers
        List<BigDecimal> installmentValues = getInstallmentsValues(transferDTO);
        BigDecimal installmentValue = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Transfer txOrigin = null;
        Transfer txDestiny = null;

        // Walks throught the installments to execute the transfers
        for (int i = 0; i < installmentValues.size(); i++) {
            installmentValue = installmentValues.get(i);

            // Change the transfer date in case of installments, except the first
            if (i != 0) {
                calendar.setTime(dateFormat.parse(transferDTO.getDate()));
                calendar.add(Calendar.MONTH, 1);
                transferDTO.setDate(dateFormat.format(calendar.getTime()));
            }

            transferDTO.setAmount(installmentValue.toString());
            txOrigin = createTransferInstance(transferDTO, transferDTO.getAccOrigin(), TransferType.WITHDRAW);
            txDestiny = createTransferInstance(transferDTO, transferDTO.getAccDestiny(), TransferType.DEPOSIT);

            // Withdraw the amount from on account...
            accountService.decrementBalance(txOrigin.getAccount(), txOrigin.getAmount());
            transferRepository.save(txOrigin);

            // ... and deposit the amount on the other account
            accountService.incrementBalance(txDestiny.getAccount(), txDestiny.getAmount());
            txDestiny.setTxOrigin(txOrigin);
            transferRepository.save(txDestiny);

            // Updates the origin transfer with details of the destiny transfer
            txOrigin.setTxDestiny(txDestiny);
            transferRepository.save(txOrigin);
        }
    }

    /**
     * Searches for all transfers registered on the system.
     * 
     * @return
     */
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    /**
     * Calculates the installment values for the transfers.
     * 
     * @param transferDTO
     * @return
     * @throws NumberFormatException
     */
    List<BigDecimal> getInstallmentsValues(TransferDTO transferDTO)
            throws NumberFormatException, InvalidOperationException {
        BigDecimal installments = new BigDecimal(transferDTO.getInstallments());
        BigDecimal amount = new BigDecimal(transferDTO.getAmount());
        BigDecimal installmentValue = null;
        List<BigDecimal> installmentsValues = new ArrayList<>();

        if (installments.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidOperationException("Installments must be a positive number.");
        }

        if (amount.remainder(installments).compareTo(BigDecimal.ZERO) == 0) {
            // Creates installment values for an exact amount division
            installmentValue = amount.divide(installments);

            for (int i = 0; i < installments.intValue(); i++) {
                installmentsValues.add(installmentValue);
            }
        } else {
            // Increases the amount of the first installment for inexact division
            BigDecimal remainder = amount.remainder(installments);
            amount = amount.subtract(remainder);
            installmentValue = amount.divide(installments);

            for (int i = 0; i < installments.intValue(); i++) {
                installmentsValues.add(installmentValue);
            }

            installmentsValues.set(0, installmentsValues.get(0).add(remainder));
        }

        return installmentsValues;
    }

    /**
     * Cancels a transfer and its related transfers.
     * 
     * @param transferId
     * @throws BusinessException
     */
    public void cancelTransfer(Long transferId) throws BusinessException {
        try {
            Transfer transfer = transferRepository.findById(transferId).orElse(null);

            boolean isTansfer = transfer != null && (transfer.getStatus().equals(TransferStatus.COMPLETED)
                    || transfer.getStatus().equals(TransferStatus.SCHEDULED)) && (transfer.getTxOrigin() != null
                            || transfer.getTxDestiny() != null);
            boolean isScheduled = transfer != null && transfer.getStatus().equals(TransferStatus.SCHEDULED);

            // Throws and InvalidOperationException for cancelling a not allowed transfer
            if (!isTansfer && !isScheduled) {
                throw new InvalidOperationException(
                        "Only scheduled operations and transfers are allowed to be cancelled.");
            }

            // Cancelling a related origin transfer
            Transfer txReferenced = transfer.getTxOrigin();
            if (txReferenced != null) {
                revertTransferValues(txReferenced);
            }

            // Cancelling a related destiny transfer
            txReferenced = transfer.getTxDestiny();
            if (txReferenced != null) {
                revertTransferValues(txReferenced);
            }

            // Cancelling the current transfer
            revertTransferValues(transfer);
        } catch (InvalidOperationException e) {
            throw new BusinessException(e);
        }

    }

    /**
     * Cancels a transfer.
     * 
     * @param transfer
     * @throws InvalidOperationException
     */
    private void revertTransferValues(Transfer transfer) throws InvalidOperationException {
        Account acc = transfer.getAccount();
        BigDecimal balance = acc.getBalance();

        // Defines the type of action based on cancelling transfer type
        if (transfer.getType().equals(TransferType.DEPOSIT)) {
            balance = balance.subtract(transfer.getAmount());
        } else if (transfer.getType().equals(TransferType.WITHDRAW)) {
            balance = balance.add(transfer.getAmount());
        } else {
            // Throws an InvalidOperationException in cases of undefined transfer type
            throw new InvalidOperationException("Invalid operation type.");
        }

        acc.setBalance(balance);
        accountService.addAccount(acc);
        transfer.setStatus(TransferStatus.CANCELED);
        transferRepository.save(transfer);
    }

    public List<Transfer> findByDate(String startDate, String endDate) throws BusinessException {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date stDate = dateFormat.parse(startDate);
            Date edDate = dateFormat.parse(endDate);

            return transferRepository.findBydateBetween(stDate, edDate);
        } catch (ParseException e) {
            throw new BusinessException("Invalid date format for the period informed. Use the format 'YYY-MM-DD'.", e);
        }
    }

}
