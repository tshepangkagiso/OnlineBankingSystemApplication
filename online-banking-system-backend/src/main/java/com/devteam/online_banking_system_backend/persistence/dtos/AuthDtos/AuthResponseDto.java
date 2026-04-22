package com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos;

import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;

import java.util.UUID;

public class AuthResponseDto
{
    private UUID accountNumber;
    private String accountHolder;
    private String email;
    private String token;

    public AuthResponseDto(){}
    public AuthResponseDto(UUID accountNumber, String accountHolder, String email, String token)
    {
        this.setAccountNumber(accountNumber);
        this.setAccountHolder(accountHolder);
        this.setEmail(email);
        this.setToken(token);
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

    public String getEmail(){return this.email;}
    public void setEmail(String email)
    {
        if(email.length() <= 12)
            throw new ClientException("Email must contain more than 12 characters.");

        this.email = email;
    }

    public String getToken(){return this.token;}
    public void setToken(String token){this.token = token;}
}
