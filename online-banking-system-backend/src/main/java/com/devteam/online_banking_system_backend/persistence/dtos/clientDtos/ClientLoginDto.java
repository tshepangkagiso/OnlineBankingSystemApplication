package com.devteam.online_banking_system_backend.persistence.dtos.clientDtos;

import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;

public class ClientLoginDto
{
    private String Password;
    private String Email;

    public ClientLoginDto(){}
    public ClientLoginDto(String password, String email)
    {
        this.setPassword(password);
        this.setEmail(email);
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
