package com.devteam.online_banking_system_backend.persistence.entities;

import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID AccountNumber;

    @Column(nullable = false)
    private String AccountHolder;

    @Column(nullable = false)
    private String Password;

    @Column(nullable = false, unique = true)
    private String Email;

    @OneToOne
    @JoinColumn(name = "SavingsAccountId")
    private SavingsAccount SavingsAccount;

    @OneToOne
    @JoinColumn(name = "CheckAccountId")
    private CheckAccount CheckAccount;

    public Client(){}
    public Client(UUID accountNumber, String accountHolder, String password, String email, SavingsAccount savingsAccount, CheckAccount checkAccount)
    {
        this.setAccountNumber(accountNumber);
        this.setAccountHolder(accountHolder);
        this.setPassword(password);
        this.setEmail(email);
    }


    public UUID getAccountNumber()
    {
        return this.AccountNumber;
    }
    public void setAccountNumber(UUID accountNumber) {this.AccountNumber = accountNumber;}

    public String getAccountHolder(){ return  this.AccountHolder; }
    public void setAccountHolder(String accountHolder)
    {
        if(accountHolder.length() <= 3)
            throw new ClientException("AccountHolder name should be more than 3 characters.");

        this.AccountHolder = accountHolder;
    }

    public String getPassword(){return this.Password;}
    public void setPassword(String password)
    {
        if(password.length() <= 12)
            throw new ClientException("Password must contain more than 12 characters.");

        this.Password = password;
    }

    public String getEmail(){return this.Email;}
    public void setEmail(String email)
    {
        if(email.length() <= 12)
            throw new ClientException("Email must contain more than 12 characters.");

        this.Email = email;
    }

    public SavingsAccount getSavingsAccount(){return this.SavingsAccount;}
    public void setSavingsAccount(SavingsAccount savingsAccount){ this.SavingsAccount = savingsAccount;}

    public CheckAccount getCheckAccount(){return this.CheckAccount;}
    public void setCheckAccount(CheckAccount checkAccount){this.CheckAccount = checkAccount;}

}
