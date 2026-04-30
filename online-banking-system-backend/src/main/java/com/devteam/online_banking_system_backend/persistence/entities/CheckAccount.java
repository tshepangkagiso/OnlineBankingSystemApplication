package com.devteam.online_banking_system_backend.persistence.entities;

import com.devteam.online_banking_system_backend.persistence.exceptions.AccountException;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "checkAccount")
public class CheckAccount extends Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkAccountId;
    private BigDecimal overDraftLimit = new BigDecimal("50000.0"); //An overdraft limit is the maximum amount of money your bank allows you to borrow automatically when your account goes below zero

    public CheckAccount(){}
    public CheckAccount(Long checkAccountId, BigDecimal balance, BigDecimal overDraftLimit)
    {
        this.setCheckAccountId(checkAccountId);
        this.setBalance(balance);
        this.setOverdraftLimit(overDraftLimit);
    }

    public Long getCheckAccountId(){return checkAccountId;}
    public void setCheckAccountId(Long checkAccountId){this.checkAccountId = checkAccountId;}

    public BigDecimal getOverdraftLimit(){return this.overDraftLimit;}
    public void setOverdraftLimit(BigDecimal overDraftLimit){this.overDraftLimit = overDraftLimit;}


    //Account Methods
    private BigDecimal ApplyOverdraft(BigDecimal withdrawAmount, BigDecimal currentBalance)
    {
        if((currentBalance.subtract(withdrawAmount).abs().compareTo(getOverdraftLimit()) > 0))
            throw new AccountException("Your account has reached an overdraft limit!");

        return currentBalance.subtract(withdrawAmount);
    }

    @Override
    public BigDecimal Deposit(BigDecimal depositAmount)
    {
        if(depositAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new AccountException("Cannot deposit amount equal or less than zero.");
        return this.getBalance().add(depositAmount);
    }

    @Override
    public BigDecimal Withdraw(BigDecimal withdrawAmount)
    {
        if(withdrawAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new AccountException("Cannot withdraw a zero or less amount. ");

        return this.ApplyOverdraft(withdrawAmount, this.getBalance());
    }

}