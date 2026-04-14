package com.devteam.online_banking_system_backend.persistence.exceptions;

public class ClientException extends RuntimeException
{
    public ClientException (String message)
    {
        super(message);
    }
}
