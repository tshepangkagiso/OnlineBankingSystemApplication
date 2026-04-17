package com.devteam.online_banking_system_backend.persistence.dtos.clientDtos;

import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;

public class ClientRegisterDto
{
    private String accountHolder;
    private String password;
    private String email;

    public ClientRegisterDto(){}
    public ClientRegisterDto(String accountHolder, String password, String email)
    {
        this.setAccountHolder(accountHolder);
        this.setPassword(password);
        this.setEmail(email);
    }

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
}
