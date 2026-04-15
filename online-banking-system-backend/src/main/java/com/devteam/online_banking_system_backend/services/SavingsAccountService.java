package com.devteam.online_banking_system_backend.services;

import com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos.SavingsTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.persistence.repositories.ISavingsAccountRepository;
import com.devteam.online_banking_system_backend.services.exceptions.SavingsAccountServiceException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SavingsAccountService
{
    private final ISavingsAccountRepository repository;

    public SavingsAccountService(ISavingsAccountRepository repository)
    {
        this.repository = repository;
    }

    //get savings account by ID
    public SavingsAccount getSavingsAccountByID(Long id)
    {
        Optional<SavingsAccount> account = repository.findById(id);
        if(account.isEmpty())
            throw new SavingsAccountServiceException("No savings account with that savings account id was found.");
        return account.get();
    }

    //deposit into savings account (update)
    public SavingsAccount deposit(SavingsTransactionDto dto)
    {
        SavingsAccount account = getSavingsAccountByID( dto.getSavingsAccountId() );
        account.setBalance( account.Deposit( dto.getAmount()) );
        return repository.save(account);
    }

    //withdraw out of savings account (update)
    public SavingsAccount withdraw(SavingsTransactionDto dto)
    {
        SavingsAccount account = getSavingsAccountByID( dto.getSavingsAccountId() );
        account.setBalance( account.Withdraw( dto.getAmount()) );
        return repository.save(account);
    }

    //apply interest to balance in savings account (update)
    public SavingsAccount applyInterest(SavingsAccount account)
    {
        account.ApplyInterest();
        return repository.save(account);
    }
}
