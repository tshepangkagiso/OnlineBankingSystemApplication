package com.devteam.online_banking_system_backend.persistence.repositories;

import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import org.springframework.data.repository.CrudRepository;

public interface ISavingsAccountRepository extends CrudRepository<SavingsAccount,Long>
{
}
