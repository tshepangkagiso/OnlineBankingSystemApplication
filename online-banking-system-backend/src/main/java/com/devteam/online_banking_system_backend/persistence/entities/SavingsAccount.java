package com.devteam.online_banking_system_backend.persistence.entities;

import com.devteam.online_banking_system_backend.persistence.exceptions.AccountException;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "savingsAccount")
public class SavingsAccount extends Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SavingsAccountId;
    private Double InterestRate = 3.5; //add extra money to your savings based on how much you have deposited
    private LocalDate LatestDepositDate;

    public SavingsAccount(){}
    public SavingsAccount(Long savingsAccountId,Double balance, Double interestRate, LocalDate latestDepositDate)
    {
        this.setSavingsAccountId(savingsAccountId);
        this.setBalance(balance);
        this.setInterestRate(interestRate);
        this.setLatestDepositDate(latestDepositDate);
    }

    public Long getSavingsAccountId(){return this.SavingsAccountId;}
    public void setSavingsAccountId(Long savingsAccountId){this.SavingsAccountId = savingsAccountId;}

    public Double getInterestRate()
    {
        return this.InterestRate;
    }
    public void setInterestRate(Double interestRate)
    {
        this.InterestRate = interestRate;
    }

    public LocalDate getLatestDepositDate(){return this.LatestDepositDate;}
    public void setLatestDepositDate(LocalDate latestDepositDate){ this.LatestDepositDate = latestDepositDate;}


    //Account Methods
    public void ApplyInterest()
    {
        if(getLatestDepositDate().isBefore( getLatestDepositDate().plusMonths(1) ))
        {
            double newBalance = getBalance() + (getBalance() * getInterestRate());
            this.setBalance(newBalance);
        }
    }

    @Override
    public Double Deposit(Double depositAmount, Double currentBalance)
    {
        if(depositAmount <= 0)
            throw new AccountException("Cannot deposit amount less or equal to zero.");
        return depositAmount + currentBalance;
    }

    @Override
    public Double Withdraw(Double withdrawAmount, Double currentBalance)
    {
        if(currentBalance < withdrawAmount)
            throw new AccountException("Cannot withdraw more than you currently have. Balance: R" + currentBalance);
        return 0.0;
    }

}
