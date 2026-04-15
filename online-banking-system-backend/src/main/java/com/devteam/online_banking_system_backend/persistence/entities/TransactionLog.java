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
    private Long TransactionLogId;
    private LocalDateTime TransactionDateTime;
    private String ClientEmail;
    private ACCOUNTTYPE AccountType;
    private TRANSACTIONTYPE TransactionType;
    private BigDecimal PreTransactionBalance;
    private BigDecimal PostTransactionBalance;


    public TransactionLog() {}

    public TransactionLog(Long transactionLogId, LocalDateTime transactionDateTime, String clientEmail,
                          ACCOUNTTYPE accountType, TRANSACTIONTYPE transactionType,
                          BigDecimal preTransactionBalance, BigDecimal postTransactionBalance)
    {
        setTransactionLogId(transactionLogId);
        setTransactionDateTime(transactionDateTime);
        setClientEmail(clientEmail);
        setAccountType(accountType);
        setTransactionType(transactionType);
        setPreTransactionBalance(preTransactionBalance);
        setPostTransactionBalance(postTransactionBalance);
    }


    public Long getTransactionLogId() { return TransactionLogId; }

    public void setTransactionLogId(Long transactionLogId)
    {
        if (transactionLogId == null)
            throw new TransactionLogException("Transaction Log ID cannot be null.");
        this.TransactionLogId = transactionLogId;
    }

    public LocalDateTime getTransactionDateTime() { return TransactionDateTime; }

    public void setTransactionDateTime(LocalDateTime transactionDateTime)
    {
        if (transactionDateTime == null)
            throw new TransactionLogException("Transaction timestamp cannot be null.");

        this.TransactionDateTime = transactionDateTime;
    }

    public String getClientEmail() { return ClientEmail; }

    public void setClientEmail(String clientEmail)
    {
        if (clientEmail == null || clientEmail.isBlank())
            throw new TransactionLogException("Client email cannot be null or empty.");
        this.ClientEmail = clientEmail;
    }

    public ACCOUNTTYPE getAccountType() { return AccountType; }

    public void setAccountType(ACCOUNTTYPE accountType) {
        if (accountType == null)
            throw new TransactionLogException("Account type must be specified.");
        this.AccountType = accountType;
    }

    public TRANSACTIONTYPE getTransactionType() { return TransactionType; }

    public void setTransactionType(TRANSACTIONTYPE transactionType)
    {
        if (transactionType == null)
            throw new TransactionLogException("Transaction type must be specified.");
        this.TransactionType = transactionType;
    }

    public BigDecimal getPreTransactionBalance() { return PreTransactionBalance; }

    public void setPreTransactionBalance(BigDecimal preTransactionBalance)
    {
        if (preTransactionBalance == null)
            throw new TransactionLogException("Pre-transaction balance cannot be null.");
        this.PreTransactionBalance = preTransactionBalance;
    }

    public BigDecimal getPostTransactionBalance() { return PostTransactionBalance; }

    public void setPostTransactionBalance(BigDecimal postTransactionBalance)
    {
        if (postTransactionBalance == null)
            throw new TransactionLogException("Post-transaction balance cannot be null.");
        this.PostTransactionBalance = postTransactionBalance;
    }

}
