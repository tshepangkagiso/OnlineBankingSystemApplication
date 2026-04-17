package com.devteam.online_banking_system_backend.persistence.entities;

import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import com.devteam.online_banking_system_backend.persistence.exceptions.TransactionLogException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactionLogs")
public class TransactionLog
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionLogId;
    private LocalDateTime transactionDatetime;
    private String clientEmail;
    private ACCOUNTTYPE accountType;
    private TRANSACTIONTYPE transactionType;
    private BigDecimal preTransactionBalance;
    private BigDecimal postTransactionBalance;


    public TransactionLog() {}

    public TransactionLog(Long transactionLogId, LocalDateTime transactionDatetime, String clientEmail,
                          ACCOUNTTYPE accountType, TRANSACTIONTYPE transactionType,
                          BigDecimal preTransactionBalance, BigDecimal postTransactionBalance)
    {
        setTransactionLogId(transactionLogId);
        setTransactionDatetime(transactionDatetime);
        setClientEmail(clientEmail);
        setAccountType(accountType);
        setTransactionType(transactionType);
        setPreTransactionBalance(preTransactionBalance);
        setPostTransactionBalance(postTransactionBalance);
    }


    public Long getTransactionLogId() { return transactionLogId; }

    public void setTransactionLogId(Long transactionLogId)
    {
        this.transactionLogId = transactionLogId;
    }

    public LocalDateTime getTransactionDatetime() { return transactionDatetime; }

    public void setTransactionDatetime(LocalDateTime transactionDatetime)
    {
        if (transactionDatetime == null)
            throw new TransactionLogException("Transaction timestamp cannot be null.");

        this.transactionDatetime = transactionDatetime;
    }

    public String getClientEmail() { return clientEmail; }

    public void setClientEmail(String clientEmail)
    {
        if (clientEmail == null || clientEmail.isBlank())
            throw new TransactionLogException("Client email cannot be null or empty.");
        this.clientEmail = clientEmail;
    }

    public ACCOUNTTYPE getAccountType() { return accountType; }

    public void setAccountType(ACCOUNTTYPE accountType) {
        if (accountType == null)
            throw new TransactionLogException("Account type must be specified.");
        this.accountType = accountType;
    }

    public TRANSACTIONTYPE getTransactionType() { return transactionType; }

    public void setTransactionType(TRANSACTIONTYPE transactionType)
    {
        if (transactionType == null)
            throw new TransactionLogException("Transaction type must be specified.");
        this.transactionType = transactionType;
    }

    public BigDecimal getPreTransactionBalance() { return preTransactionBalance; }

    public void setPreTransactionBalance(BigDecimal preTransactionBalance)
    {
        if (preTransactionBalance == null)
            throw new TransactionLogException("Pre-transaction balance cannot be null.");
        this.preTransactionBalance = preTransactionBalance;
    }

    public BigDecimal getPostTransactionBalance() { return postTransactionBalance; }

    public void setPostTransactionBalance(BigDecimal postTransactionBalance)
    {
        if (postTransactionBalance == null)
            throw new TransactionLogException("Post-transaction balance cannot be null.");
        this.postTransactionBalance = postTransactionBalance;
    }

}
