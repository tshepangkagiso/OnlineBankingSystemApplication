package com.devteam.online_banking_system_backend.services;

import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthRequestDto;
import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthResponseDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import com.devteam.online_banking_system_backend.persistence.exceptions.ClientException;
import com.devteam.online_banking_system_backend.persistence.repositories.IClientRepository;
import com.devteam.online_banking_system_backend.security.PasswordService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ClientService
{
    private final IClientRepository repository;
    private final PasswordService passwordService;
    private final TransactionLogService transactionLogService;

    public ClientService(IClientRepository repository, PasswordService passwordService, TransactionLogService transactionLogService)
    {
        this.repository = repository;
        this.passwordService = passwordService;
        this.transactionLogService = transactionLogService;
    }


    //For READ
    public Client findClientByEmail(String email)
    {
        Optional<Client> foundClient = repository.findByEmail(email);
        if(foundClient.isEmpty())
            throw new ClientException("No client was found with that email.");
        return foundClient.get();
    }

    //For WRITE
    public Client findClientByEmailWRITE(String email)
    {
        Optional<Client> foundClient = repository.findByEmailWRITEOPERATION(email);
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
        Client client = new Client(dto.getAccountHolder(),hashedPassword, dto.getEmail());
        return repository.save(client);
    }

    public Client updateClient(String email ,Client client)
    {
        Client foundClient = findClientByEmailWRITE(email);
        foundClient.setAccountHolder(client.getAccountHolder());
        foundClient.setEmail(client.getEmail());
        foundClient.setPassword(client.getPassword());
        foundClient.setSavingsAccount(client.getSavingsAccount());
        foundClient.setCheckAccount(client.getCheckAccount());
        return repository.save(foundClient);
    }

    //login
    public AuthResponseDto login(AuthRequestDto dto)
    {
        Client client = findClientByEmail(dto.getEmail());
        if(!passwordService.isVerifiedPassword(dto.getPassword(), client.getPassword()))
            throw new ClientException("Incorrect Password, please try again.");

        String token = "jbcshcsjcnsksmcskmcskcmskcmskcmskcsmcksmskcskcmskcmcksmskcsc";
        return new AuthResponseDto(client.getAccountNumber(), client.getAccountHolder(), client.getEmail(), token);
    }

    //open a new savings account
    public void clientOpenSavingsAccount(OpenAccountDto dto)
    {
        Client client = findClientByEmailWRITE(dto.getEmail());
        SavingsAccount savingsAccount = new SavingsAccount(LocalDate.now());
        client.setSavingsAccount(savingsAccount);
        repository.save(client);

        transactionLogService.recordTransaction(new TransactionLog(
                null,
                LocalDateTime.now(),
                client.getEmail(),
                ACCOUNTTYPE.SAVINGSACCOUNT,
                TRANSACTIONTYPE.ACCOUNTOPENING,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        ));
    }

    //open a new check account
    public void clientOpenCheckAccount(OpenAccountDto dto)
    {
        Client client= findClientByEmailWRITE(dto.getEmail());
        CheckAccount checkAccount = new CheckAccount();
        client.setCheckAccount(checkAccount);
        repository.save(client);

        transactionLogService.recordTransaction(new TransactionLog(
                null,
                LocalDateTime.now(),
                client.getEmail(),
                ACCOUNTTYPE.CHECKACCOUNT,
                TRANSACTIONTYPE.ACCOUNTOPENING,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        ));
    }

}