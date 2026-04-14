package com.devteam.online_banking_system_backend.persistence.repositories;

import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import org.springframework.data.repository.CrudRepository;

public interface ICheckAccountRepository extends CrudRepository<CheckAccount,Long>
{
}
