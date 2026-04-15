package com.devteam.online_banking_system_backend.persistence.repositories;

import com.devteam.online_banking_system_backend.persistence.entities.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface IClientRepository extends CrudRepository<Client, UUID>
{
    @Query("SELECT c FROM Client c WHERE c.Email = :email")
    Optional<Client> findByEmail(@Param("email") String email);
}
