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
    private Long savingsAccountId;
    private BigDecimal interestRate; //add extra money (3.5%) to your savings based on how much you have deposited
    private LocalDate latestDepositDate;

    // Constructors
    public SavingsAccount() {
        // Default constructor - interestRate will be initialized by @PostLoad
    }

    public SavingsAccount(Long savingsAccountId, BigDecimal balance, BigDecimal interestRate, LocalDate latestDepositDate)
    {
        this.setSavingsAccountId(savingsAccountId);
        this.setBalance(balance);
        if (interestRate != null) {
            this.setInterestRate(interestRate);
        }
        this.setLatestDepositDate(latestDepositDate);
    }

    public SavingsAccount(LocalDate latestDepositDate)
    {
        this.setLatestDepositDate(latestDepositDate);
    }

    // Helper method to get default daily interest rate (3.5% annual / 31 days)
    private BigDecimal getDefaultInterestRate() {
        return new BigDecimal("0.035").divide(new BigDecimal("31"), 10, RoundingMode.HALF_UP);
    }

    // JPA lifecycle callbacks
    @PostLoad
    @PrePersist
    private void ensureInterestRate() {
        if (this.interestRate == null || this.interestRate.compareTo(BigDecimal.ZERO) == 0) {
            this.interestRate = getDefaultInterestRate();
        }
    }

    // Getters and Setters
    public Long getSavingsAccountId() {
        return this.savingsAccountId;
    }

    public void setSavingsAccountId(Long savingsAccountId) {
        this.savingsAccountId = savingsAccountId;
    }

    public BigDecimal getInterestRate() {
        // Safety check in case @PostLoad didn't run
        if (this.interestRate == null || this.interestRate.compareTo(BigDecimal.ZERO) == 0) {
            return getDefaultInterestRate();
        }
        return this.interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getLatestDepositDate() {
        return this.latestDepositDate;
    }

    public void setLatestDepositDate(LocalDate latestDepositDate) {
        this.latestDepositDate = latestDepositDate;
    }

    // Account Methods
    public void ApplyInterest() {
        BigDecimal rate = getInterestRate(); // This will always return a valid rate
        if (getBalance() == null) {
            this.setBalance(BigDecimal.ZERO);
        }
        BigDecimal newBalance = getBalance().add(getBalance().multiply(rate));
        this.setBalance(newBalance.setScale(2, RoundingMode.HALF_EVEN));
    }

    @Override
    public BigDecimal Deposit(BigDecimal depositAmount) {
        if (depositAmount == null || depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountException("Cannot deposit amount less than or equal to zero.");
        }
        if (getBalance() == null) {
            this.setBalance(BigDecimal.ZERO);
        }
        BigDecimal newBalance = getBalance().add(depositAmount);
        this.setBalance(newBalance);
        return newBalance;
    }

    @Override
    public BigDecimal Withdraw(BigDecimal withdrawAmount) {
        if (withdrawAmount == null || withdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountException("Cannot withdraw amount less than or equal to zero.");
        }
        if (getBalance() == null) {
            this.setBalance(BigDecimal.ZERO);
        }
        if (this.getBalance().compareTo(withdrawAmount) < 0) {
            throw new AccountException("Cannot withdraw more than you currently have. Balance: R" + this.getBalance());
        }
        BigDecimal newBalance = this.getBalance().subtract(withdrawAmount);
        this.setBalance(newBalance);
        return newBalance;
    }
}
