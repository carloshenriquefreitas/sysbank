package com.chfreitas.sysbank.dto;

/**
 * Data Transfer Object used to simplify the receivement of attributes from
 * requests related to the Account entity.
 */
public class AccountDTO {

    private String principalCPF;
    private String description;

    public String getPrincipalCPF() {
        return principalCPF;
    }

    public void setPrincipalCPF(String principalCPF) {
        this.principalCPF = principalCPF;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
