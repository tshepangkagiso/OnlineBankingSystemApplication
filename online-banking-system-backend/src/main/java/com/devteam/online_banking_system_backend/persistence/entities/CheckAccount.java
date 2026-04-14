package com.devteam.online_banking_system_backend.persistence.entities;

import com.devteam.online_banking_system_backend.persistence.exceptions.AccountException;
import jakarta.persistence.*;

@Entity
@Table(name = "checkAccount")
public class CheckAccount extends Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CheckAccountId;
    private Double OverdraftLimit = 300.0; //An overdraft limit is the maximum amount of money your bank allows you to borrow automatically when your account goes below zero

    public CheckAccount(){}
    public CheckAccount(Long checkAccountId,Double balance, Double overdraftLimit)
    {
        this.setCheckAccountId(checkAccountId);
        this.setBalance(balance);
        this.setOverdraftLimit(overdraftLimit);
    }

    public Long getCheckAccountId(){return CheckAccountId;}
    public void setCheckAccountId(Long checkAccountId){this.CheckAccountId = checkAccountId;}

    public Double getOverdraftLimit(){return this.OverdraftLimit;}
    public void setOverdraftLimit(Double overdraftLimit){this.OverdraftLimit = overdraftLimit;}


    //Account Methods
    private Double ApplyOverdraft(Double withdrawAmount, Double currentBalance)
    {
        if((Math.abs(currentBalance - withdrawAmount) > getOverdraftLimit()))
            throw new AccountException("Your account has reached an overdraft limit!");

        return currentBalance - withdrawAmount;
    }

    @Override
    public Double Deposit(Double depositAmount, Double currentBalance)
    {
        if(depositAmount <= 0)
            throw new AccountException("Cannot deposit amount equal or less than zero.");
        return currentBalance + depositAmount;
    }

    @Override
    public Double Withdraw(Double withdrawAmount, Double currentBalance)
    {
        if(withdrawAmount <= 0.0)
            throw new AccountException("Cannot withdraw a zero or less amount. ");

        return this.ApplyOverdraft(withdrawAmount, currentBalance);
    }

}
