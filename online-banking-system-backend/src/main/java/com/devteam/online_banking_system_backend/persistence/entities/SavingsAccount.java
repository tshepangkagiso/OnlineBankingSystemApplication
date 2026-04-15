package com.devteam.online_banking_system_backend.persistence.entities;

import com.devteam.online_banking_system_backend.persistence.exceptions.AccountException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


@Entity
@Table(name = "savingsAccount")
public class SavingsAccount extends Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SavingsAccountId;
    private BigDecimal InterestRate = new BigDecimal("0.035").divide(new BigDecimal("31"), 10, RoundingMode.HALF_UP); //add extra money (3.5%) to your savings based on how much you have deposited
    private LocalDate LatestDepositDate;

    public SavingsAccount(){}
    public SavingsAccount(Long savingsAccountId, BigDecimal balance, BigDecimal interestRate, LocalDate latestDepositDate)
    {
        this.setSavingsAccountId(savingsAccountId);
        this.setBalance(balance);
        this.setInterestRate(interestRate);
        this.setLatestDepositDate(latestDepositDate);
    }

    public SavingsAccount(LocalDate latestDepositDate)
    {
        this.setLatestDepositDate(latestDepositDate);
    }

    public Long getSavingsAccountId(){return this.SavingsAccountId;}
    public void setSavingsAccountId(Long savingsAccountId){this.SavingsAccountId = savingsAccountId;}

    public BigDecimal getInterestRate()
    {
        return this.InterestRate;
    }
    public void setInterestRate(BigDecimal interestRate)
    {
        this.InterestRate = interestRate;
    }

    public LocalDate getLatestDepositDate(){return this.LatestDepositDate;}
    public void setLatestDepositDate(LocalDate latestDepositDate){ this.LatestDepositDate = latestDepositDate;}


    //Account Methods
    public void ApplyInterest()
    {
        BigDecimal newBalance = getBalance().add(getBalance().multiply(getInterestRate()));
        this.setBalance(newBalance.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Override
    public BigDecimal Deposit(BigDecimal depositAmount)
    {
        if(depositAmount.compareTo(BigDecimal.ZERO) <= 0)
            throw new AccountException("Cannot deposit amount less or equal to zero.");
        return depositAmount.add(this.getBalance());
    }

    @Override
    public BigDecimal Withdraw(BigDecimal withdrawAmount)
    {
        if(this.getBalance().compareTo(withdrawAmount) < 0)
            throw new AccountException("Cannot withdraw more than you currently have. Balance: R" + this.getBalance());

        return this.getBalance().subtract(withdrawAmount);
    }

}
