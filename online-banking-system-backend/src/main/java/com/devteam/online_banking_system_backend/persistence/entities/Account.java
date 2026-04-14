package com.devteam.online_banking_system_backend.persistence.entities;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Account
{
    private Double Balance = 0.0;

    abstract Double Deposit(Double depositAmount, Double currentBalance);
    abstract Double Withdraw(Double withdrawAmount, Double currentBalance);

    public Double getBalance() { return this.Balance; }
    public void setBalance(Double balance) { this.Balance = balance; }
}

