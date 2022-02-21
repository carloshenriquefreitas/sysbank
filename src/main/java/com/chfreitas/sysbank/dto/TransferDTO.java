package com.chfreitas.sysbank.dto;

/**
 * Data Transfer Object used to simplify the receivement of attributes from
 * requests related to the Transfer entity.
 */
public class TransferDTO {

    private String type;
    private String date;
    private String amount;
    private String installments;
    private String accOrigin;
    private String accDestiny;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInstallments() {
        return installments;
    }

    public void setInstallments(String installments) {
        this.installments = installments;
    }

    public String getAccOrigin() {
        return accOrigin;
    }

    public void setAccOrigin(String accOrigin) {
        this.accOrigin = accOrigin;
    }

    public String getAccDestiny() {
        return accDestiny;
    }

    public void setAccDestiny(String accDestiny) {
        this.accDestiny = accDestiny;
    }

}
