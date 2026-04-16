package com.devteam.online_banking_system_backend.services;

import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import com.devteam.online_banking_system_backend.persistence.repositories.ITransactionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionLogService
{

    private final ITransactionLogRepository repository;

    public TransactionLogService(ITransactionLogRepository repository) {
        this.repository = repository;
    }

    // --- Creation ---

    @Transactional
    public void recordTransaction(TransactionLog log) {
        repository.save(log);
    }

    @Transactional
    public void recordInterestEntry(TransactionLog interestLog) {
        repository.save(interestLog);
    }

    // --- Client Facing ---

    @Transactional(readOnly = true)
    public Iterable<TransactionLog> getTransactionHistoryByEmail(String email) {
        return repository.findTransactionLogsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Iterable<TransactionLog> getHistoryByAccountType(String email, ACCOUNTTYPE type) {
        return repository.findTransactionLogsByAccountType(email, type);
    }

    @Transactional(readOnly = true)
    public Iterable<TransactionLog> getRecentTransactions(String email) {
        return repository.findRecentTransactions(email);
    }

    // --- Admin Facing ---

    @Transactional(readOnly = true)
    public Iterable<TransactionLog> findTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByDateRange(start, end);
    }

    @Transactional(readOnly = true)
    public Iterable<TransactionLog> findTransactionsByType(TRANSACTIONTYPE type) {
        return repository.findByTransactionType(type);
    }

    @Transactional(readOnly = true)
    public Iterable<TransactionLog> findHighValueTransactions(BigDecimal threshold) {
        return repository.findHighValueTransactions(threshold);
    }
}
