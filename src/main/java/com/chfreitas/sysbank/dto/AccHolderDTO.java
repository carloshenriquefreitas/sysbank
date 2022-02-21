package com.chfreitas.sysbank.dto;

/**
 * Data Transfer Object used to simplify the receivement of attributes from
 * requests related to the Account Holder entity.
 */
public class AccHolderDTO {

    private String name;
    private String cpf;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}
