package com.devteam.online_banking_system_backend.persistence.repositories;

import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ITransactionLogRepository extends CrudRepository<TransactionLog, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TransactionLog t WHERE t.transactionLogId = :id")
    Optional<TransactionLog> findByIdWRITEOPERATIONS(@Param("id") Long id);

    // --- Client Facing ---

    @Query("SELECT t FROM TransactionLog t WHERE t.clientEmail = :email ORDER BY t.transactionDatetime DESC")
    Iterable<TransactionLog> findTransactionLogsByEmail(@Param("email") String email);

    @Query("SELECT t FROM TransactionLog t WHERE t.clientEmail = :email AND t.accountType = :type ORDER BY t.transactionDatetime DESC")
    Iterable<TransactionLog> findTransactionLogsByAccountType(@Param("email") String email, @Param("type") ACCOUNTTYPE type);

    @Query("SELECT t FROM TransactionLog t WHERE t.clientEmail = :email ORDER BY t.transactionDatetime DESC")
    Iterable<TransactionLog> findRecentTransactions(@Param("email") String email);

    // --- Admin Facing ---

    @Query("SELECT t FROM TransactionLog t WHERE t.transactionDatetime BETWEEN :startDate AND :endDate ORDER BY t.transactionDatetime DESC")
    Iterable<TransactionLog> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t FROM TransactionLog t WHERE t.transactionType = :type")
    Iterable<TransactionLog> findByTransactionType(@Param("type") TRANSACTIONTYPE type);

    @Query("SELECT t FROM TransactionLog t WHERE t.postTransactionBalance >= :threshold OR t.preTransactionBalance >= :threshold ORDER BY t.transactionDatetime DESC")
    Iterable<TransactionLog> findHighValueTransactions(@Param("threshold") BigDecimal threshold);
}
