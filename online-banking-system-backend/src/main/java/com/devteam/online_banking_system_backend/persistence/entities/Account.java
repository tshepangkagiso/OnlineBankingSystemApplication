package com.devteam.online_banking_system_backend.persistence.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@MappedSuperclass
public abstract class Account
{
    private BigDecimal balance = BigDecimal.ZERO;

    abstract BigDecimal Deposit(BigDecimal depositAmount);
    abstract BigDecimal Withdraw(BigDecimal withdrawAmount);

    public BigDecimal getBalance() { return this.balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}

