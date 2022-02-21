package com.chfreitas.sysbank.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity that represents a Transfer into the system.
 */
@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false)
    private TransferType type;

    @Column(name = "status", nullable = false)
    private TransferStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Account account;

    @OneToOne(optional = true)
    @JsonIgnore
    private Transfer txOrigin;

    @OneToOne(optional = true)
    @JsonIgnore
    private Transfer txDestiny;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransferType getType() {
        return type;
    }

    public void setType(TransferType type) {
        this.type = type;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Transfer getTxOrigin() {
        return txOrigin;
    }

    public void setTxOrigin(Transfer txOrigin) {
        this.txOrigin = txOrigin;
    }

    public Transfer getTxDestiny() {
        return txDestiny;
    }

    public void setTxDestiny(Transfer txDestiny) {
        this.txDestiny = txDestiny;
    }

}
