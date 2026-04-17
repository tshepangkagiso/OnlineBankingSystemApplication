package com.devteam.online_banking_system_backend.persistence.dtos.clientDtos;

import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;

public class OpenAccountDto
{
    private String email;

    public OpenAccountDto(){}
    public OpenAccountDto(String email)
    {
        this.setEmail(email);
    }

    public String getEmail(){return this.email;}
    public void setEmail(String email)
    {
        if(email.length() <= 12)
            throw new ClientException("Email must contain more than 12 characters.");

        this.email = email;
    }
}
