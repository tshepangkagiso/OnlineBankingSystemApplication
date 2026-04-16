package com.devteam.online_banking_system_backend.services;

import com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos.SavingsTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import com.devteam.online_banking_system_backend.persistence.repositories.ISavingsAccountRepository;
import com.devteam.online_banking_system_backend.services.exceptions.SavingsAccountServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class SavingsAccountService
{
    private final ISavingsAccountRepository repository;
    private final TransactionLogService transactionLogService;

    public SavingsAccountService(ISavingsAccountRepository repository, TransactionLogService transactionLogService)
    {
        this.repository = repository;
        this.transactionLogService = transactionLogService;
    }

    //get savings account by ID READ
    public SavingsAccount getSavingsAccountByID(Long id)
    {
        Optional<SavingsAccount> account = repository.findById(id);
        if(account.isEmpty())
            throw new SavingsAccountServiceException("No savings account with that savings account id was found.");
        return account.get();
    }

    //get savings account by ID WRITE
    public SavingsAccount getSavingsAccountByIDWRITE(Long id)
    {
        Optional<SavingsAccount> account = repository.findByIdWRITEOPERATIONS(id);
        if(account.isEmpty())
            throw new SavingsAccountServiceException("No savings account with that savings account id was found.");
        return account.get();
    }

    //deposit into savings account (update)
    public SavingsAccount deposit(SavingsTransactionDto dto, String email)
    {
        SavingsAccount account = getSavingsAccountByIDWRITE( dto.getSavingsAccountId() );

        BigDecimal preBalance = account.getBalance();
        BigDecimal postBalance = account.Deposit( dto.getAmount());
        account.setBalance(postBalance);

        SavingsAccount savedAccount = repository.save(account);

        transactionLogService.recordTransaction(new TransactionLog(
                null,
                LocalDateTime.now(),
                email,
                ACCOUNTTYPE.SAVINGSACCOUNT,
                TRANSACTIONTYPE.DEPOSIT,
                preBalance,
                postBalance
        ));

        return savedAccount;
    }

    //withdraw out of savings account (update)
    public SavingsAccount withdraw(SavingsTransactionDto dto , String email)
    {
        SavingsAccount account = getSavingsAccountByIDWRITE( dto.getSavingsAccountId() );

        BigDecimal preBalance = account.getBalance();
        BigDecimal postBalance = account.Withdraw( dto.getAmount());
        account.setBalance(postBalance);

        SavingsAccount savedAccount = repository.save(account);

        transactionLogService.recordTransaction(new TransactionLog(
                null,
                LocalDateTime.now(),
                email,
                ACCOUNTTYPE.SAVINGSACCOUNT,
                TRANSACTIONTYPE.WITHDRAWAL,
                preBalance,
                postBalance
        ));

        return savedAccount;
    }

    //apply interest to balance in savings account (update)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SavingsAccount applyInterest(SavingsAccount account)
    {
        account.ApplyInterest();
        return repository.save(account);
    }
}
