package com.devteam.online_banking_system_backend.services;

import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.CheckTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.repositories.ICheckAccountRepository;
import com.devteam.online_banking_system_backend.services.exceptions.CheckingAccountServiceException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckAccountService
{
    private final ICheckAccountRepository repository;

    public CheckAccountService(ICheckAccountRepository repository)
    {
        this.repository = repository;
    }

    //get check account by ID
    public CheckAccount getCheckAccountById(Long id)
    {
        Optional<CheckAccount> foundAccount = repository.findById(id);
        if(foundAccount.isEmpty())
            throw new CheckingAccountServiceException("No check account with that id was found.");
        return foundAccount.get();
    }

    //deposit into check account (update)
    public CheckAccount deposit(CheckTransactionDto dto)
    {
        CheckAccount account = getCheckAccountById( dto.getCheckAccountId() );
        account.setBalance( account.Deposit(dto.getAmount()) );
        return repository.save(account);
    }

    //withdraw out check account (update)
    public CheckAccount withdraw(CheckTransactionDto dto)
    {
        CheckAccount account = getCheckAccountById(dto.getCheckAccountId());
        account.setBalance( account.Withdraw(dto.getAmount()));
        return repository.save(account);
    }
}
