package com.devteam.online_banking_system_backend.services.exceptions;

public class ClientServiceException extends RuntimeException
{
    public ClientServiceException(String message)
    {
        super(message);
    }
}
