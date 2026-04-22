package com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos;

import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;

public class AuthRequestDto
{
    private String password;
    private String email;

    public AuthRequestDto(){}
    public AuthRequestDto(String password, String email)
    {
        this.setPassword(password);
        this.setEmail(email);
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
