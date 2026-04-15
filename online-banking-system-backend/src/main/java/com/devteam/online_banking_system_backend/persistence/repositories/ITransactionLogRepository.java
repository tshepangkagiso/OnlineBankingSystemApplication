package com.devteam.online_banking_system_backend.persistence.repositories;

import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import org.springframework.data.repository.CrudRepository;

public interface ITransactionLogRepository extends CrudRepository<TransactionLog,Long>
{
}
