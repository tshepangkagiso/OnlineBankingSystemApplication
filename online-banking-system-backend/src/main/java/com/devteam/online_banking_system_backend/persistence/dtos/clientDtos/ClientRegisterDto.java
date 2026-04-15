package com.devteam.online_banking_system_backend.persistence.dtos.clientDtos;

import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;

public class ClientRegisterDto
{
    private String AccountHolder;
    private String Password;
    private String Email;

    public ClientRegisterDto(){}
    public ClientRegisterDto(String accountHolder, String password, String email)
    {
        this.setAccountHolder(accountHolder);
        this.setPassword(password);
        this.setEmail(email);
    }

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
}
