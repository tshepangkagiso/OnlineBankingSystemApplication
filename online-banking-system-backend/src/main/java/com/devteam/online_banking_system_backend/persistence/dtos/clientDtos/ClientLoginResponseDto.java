package com.devteam.online_banking_system_backend.persistence.dtos.clientDtos;

import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;

import java.util.UUID;

public class ClientLoginResponseDto
{
    private UUID AccountNumber;
    private String AccountHolder;
    private String Email;
    private String Token;

    public ClientLoginResponseDto(){}
    public ClientLoginResponseDto(UUID accountNumber, String accountHolder, String email, String token)
    {
        this.setAccountNumber(accountNumber);
        this.setAccountHolder(accountHolder);
        this.setEmail(email);
        this.setToken(token);
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

    public String getEmail(){return this.Email;}
    public void setEmail(String email)
    {
        if(email.length() <= 12)
            throw new ClientException("Email must contain more than 12 characters.");

        this.Email = email;
    }

    public String getToken(){return this.Token;}
    public void setToken(String token){this.Token = token;}
}
