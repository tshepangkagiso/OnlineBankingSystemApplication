package com.devteam.online_banking_system_backend.persistence.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@MappedSuperclass
public abstract class Account
{
    private BigDecimal Balance = BigDecimal.ZERO;

    abstract BigDecimal Deposit(BigDecimal depositAmount);
    abstract BigDecimal Withdraw(BigDecimal withdrawAmount);

    public BigDecimal getBalance() { return this.Balance; }
    public void setBalance(BigDecimal balance) { this.Balance = balance; }
}

