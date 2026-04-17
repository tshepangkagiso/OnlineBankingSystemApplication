package com.devteam.online_banking_system_backend.persistence.repositories;

import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ICheckAccountRepository extends CrudRepository<CheckAccount,Long>
{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM CheckAccount a WHERE a.checkAccountId = :id")
    Optional<CheckAccount> findByIdWRITEOPERATION(@Param("id") Long id);
}
