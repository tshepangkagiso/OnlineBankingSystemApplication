package com.devteam.online_banking_system_backend.persistence.exceptions;

public class TransactionLogException extends RuntimeException
{
    public TransactionLogException(String message)
    {
        super(message);
    }
}
