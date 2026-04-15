package com.devteam.online_banking_system_backend.services;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientLoginDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientLoginResponseDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;
import com.devteam.online_banking_system_backend.persistence.repositories.IClientRepository;
import com.devteam.online_banking_system_backend.services.securityServices.PasswordService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ClientService
{
    private final IClientRepository repository;
    private final PasswordService passwordService;

    public ClientService(IClientRepository repository, PasswordService passwordService)
    {
        this.repository = repository;
        this.passwordService = passwordService;
    }


    public Client findClientByEmail(String email)
    {
        Optional<Client> foundClient = repository.findByEmail(email);
        if(foundClient.isEmpty())
            throw new ClientException("No client was found with that email.");
        return foundClient.get();
    }

    public List<Client> getAllClients()
    {
        Iterable<Client> returnedList = repository.findAll();
        List<Client> clients = new ArrayList<>();
        returnedList.forEach(clients::add);
        return clients;
    }

    public Client registerClient(ClientRegisterDto dto)
    {
        String hashedPassword = passwordService.HashPassword(dto.getPassword());
        Client client = new Client(null, dto.getAccountHolder(),hashedPassword, dto.getEmail(),null,null);
        return repository.save(client);
    }

    //login
    public ClientLoginResponseDto login(ClientLoginDto dto)
    {
        Client client = findClientByEmail(dto.getEmail());
        if(!passwordService.isVerifiedPassword(dto.getPassword(), client.getPassword()))
            throw new ClientException("Incorrect Password, please try again.");

        String token = "jbcshcsjcnsksmcskmcskcmskcmskcmskcsmcksmskcskcmskcmcksmskcsc";
        return new ClientLoginResponseDto(client.getAccountNumber(), client.getAccountHolder(), client.getEmail(), token);
    }

    //open a new savings account
    public void clientOpenSavingsAccount(OpenAccountDto dto)
    {
        Client client = findClientByEmail(dto.getEmail());
        SavingsAccount savingsAccount = new SavingsAccount(LocalDate.now());
        client.setSavingsAccount(savingsAccount);
        repository.save(client);
    }

    //open a new check account
    public void clientOpenCheckAccount(OpenAccountDto dto)
    {
        Client client= findClientByEmail(dto.getEmail());
        CheckAccount checkAccount = new CheckAccount();
        client.setCheckAccount(checkAccount);
        repository.save(client);
    }

}
