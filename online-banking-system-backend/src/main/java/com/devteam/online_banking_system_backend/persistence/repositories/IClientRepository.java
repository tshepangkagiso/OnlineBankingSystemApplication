package com.devteam.online_banking_system_backend.persistence.repositories;

import com.devteam.online_banking_system_backend.persistence.entities.Client;
import org.springframework.data.repository.CrudRepository;
import java.util.UUID;

public interface IClientRepository extends CrudRepository<Client, UUID>
{
}
