package com.devteam.online_banking_system_backend.persistence.entities;

import com.devteam.online_banking_system_backend.persistence.enums.Role;
import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountNumber;

    @Column(nullable = false)
    private String accountHolder;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SavingsAccountId")
    private SavingsAccount savingsAccount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CheckAccountId")
    private CheckAccount checkAccount;

    @Setter
    @Getter
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    public Client(){}

    public Client(UUID accountNumber, String accountHolder, String password, String email, SavingsAccount savingsAccount, CheckAccount checkAccount, List<Role> roles)
    {
        this.setAccountNumber(accountNumber);
        this.setAccountHolder(accountHolder);
        this.setPassword(password);
        this.setEmail(email);
        this.setSavingsAccount(savingsAccount);
        this.setCheckAccount(checkAccount);
        this.setRoles(roles);
    }

    public Client(String accountHolder, String password, String email)
    {
        this.setAccountNumber(null);
        this.setAccountHolder(accountHolder);
        this.setPassword(password);
        this.setEmail(email);

        List<Role> roles = new ArrayList<>(){};
        roles.add(Role.ROLE_USER);
        this.setRoles(roles);
    }


    public UUID getAccountNumber()
    {
        return this.accountNumber;
    }
    public void setAccountNumber(UUID accountNumber) {this.accountNumber = accountNumber;}

    public String getAccountHolder(){ return  this.accountHolder; }
    public void setAccountHolder(String accountHolder)
    {
        if(accountHolder.length() <= 3)
            throw new ClientException("AccountHolder name should be more than 3 characters.");

        this.accountHolder = accountHolder;
    }

    public String getPassword(){return this.password;}
    public void setPassword(String password)
    {
        if(password.length() <= 12)
            throw new ClientException("Password must contain more than 12 characters.");

        this.password = password;
    }

    public String getEmail(){return this.email;}
    public void setEmail(String email)
    {
        if(email.length() <= 12)
            throw new ClientException("Email must contain more than 12 characters.");

        this.email = email;
    }

    public SavingsAccount getSavingsAccount(){return this.savingsAccount;}
    public void setSavingsAccount(SavingsAccount savingsAccount){ this.savingsAccount = savingsAccount;}

    public CheckAccount getCheckAccount(){return this.checkAccount;}
    public void setCheckAccount(CheckAccount checkAccount){this.checkAccount = checkAccount;}

}
