package com.devteam.online_banking_system_backend.persistence.repositories;

import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ISavingsAccountRepository extends CrudRepository<SavingsAccount,Long>
{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SavingsAccount s WHERE s.savingsAccountId = :id")
    Optional<SavingsAccount> findByIdWRITEOPERATIONS(@Param("id") Long id);
}
