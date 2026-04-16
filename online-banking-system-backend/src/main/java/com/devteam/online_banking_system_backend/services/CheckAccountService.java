package com.devteam.online_banking_system_backend.services;

import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.CheckTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import com.devteam.online_banking_system_backend.persistence.repositories.ICheckAccountRepository;
import com.devteam.online_banking_system_backend.services.exceptions.CheckingAccountServiceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class CheckAccountService
{
    private final ICheckAccountRepository repository;
    private final TransactionLogService transactionLogService;

    public CheckAccountService(ICheckAccountRepository repository, TransactionLogService transactionLogService)
    {
        this.repository = repository;
        this.transactionLogService = transactionLogService;
    }

    //get check account by ID READ
    public CheckAccount getCheckAccountById(Long id)
    {
        Optional<CheckAccount> foundAccount = repository.findById(id);
        if(foundAccount.isEmpty())
            throw new CheckingAccountServiceException("No check account with that id was found.");
        return foundAccount.get();
    }

    //get check account by ID WRITE
    public CheckAccount getCheckAccountByIdWRITE(Long id)
    {
        Optional<CheckAccount> foundAccount = repository.findByIdWRITEOPERATION(id);
        if(foundAccount.isEmpty())
            throw new CheckingAccountServiceException("No check account with that id was found.");
        return foundAccount.get();
    }

    //deposit into check account (update)
    public CheckAccount deposit(CheckTransactionDto dto, String email)
    {
        CheckAccount account = getCheckAccountByIdWRITE( dto.getCheckAccountId() );

        BigDecimal preBalance = account.getBalance();
        BigDecimal postBalance = account.Deposit(dto.getAmount());
        account.setBalance(postBalance);

        CheckAccount savedAccount = repository.save(account);

        transactionLogService.recordTransaction(new TransactionLog(
                null,
                LocalDateTime.now(),
                email,
                ACCOUNTTYPE.CHECKACCOUNT,
                TRANSACTIONTYPE.DEPOSIT,
                preBalance,
                postBalance
        ));

        return savedAccount;
    }

    //withdraw out check account (update)
    public CheckAccount withdraw(CheckTransactionDto dto, String email)
    {
        CheckAccount account = getCheckAccountByIdWRITE(dto.getCheckAccountId());

        BigDecimal preBalance = account.getBalance();
        BigDecimal postBalance = account.Withdraw(dto.getAmount());
        account.setBalance(postBalance);

        CheckAccount savedAccount = repository.save(account);

        transactionLogService.recordTransaction(new TransactionLog(
                null,
                LocalDateTime.now(),
                email,
                ACCOUNTTYPE.CHECKACCOUNT,
                TRANSACTIONTYPE.WITHDRAWAL,
                preBalance,
                postBalance
        ));

        return savedAccount;
    }
}
