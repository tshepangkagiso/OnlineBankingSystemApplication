package com.devteam.online_banking_system_backend.services;

import com.devteam.online_banking_system_backend.persistence.repositories.ITransactionLogRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogService
{
    private final ITransactionLogRepository repository;

    public TransactionLogService(ITransactionLogRepository repository)
    {
        this.repository = repository;
    }

    //Creation (SavingsAccount and CheckAccount)
    //Record Transaction
    //Record Interest Entry

    //Client (facing)
    //Get Transaction History by Email
    //Get History by Account Type
    //Get Recent Transactions

    //Admin (facing)
    //Find Transactions by Date Range
    //Find Transactions by Type
    //Find High-Value Transactions

}
