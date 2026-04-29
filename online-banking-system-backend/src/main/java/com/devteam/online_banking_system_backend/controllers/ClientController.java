package com.devteam.online_banking_system_backend.controllers;

import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthRequestDto;
import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthResponseDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clients")
public class ClientController
{
    private final ClientService clientService;

    public ClientController (ClientService clientService)
    {
        this.clientService = clientService;
    }

    //Get By Email (READ)
    @GetMapping("/{email}")
    public ResponseEntity<?> getClientByEmail(@PathVariable String email)
    {
        try
        {
            Client foundClient = this.clientService.findClientByEmail(email);
            return new ResponseEntity<>(foundClient, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //Get ALL
    @GetMapping
    public ResponseEntity<?> getAllClients()
    {
        try
        {
            List<Client> clients = this.clientService.getAllClients();
            return new ResponseEntity<>(clients, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }


    //Update Client
    @PutMapping("/update/{email}")
    public ResponseEntity<?> updateClient(@PathVariable String email, @RequestBody Client client)
    {
        try
        {
            Client updatedClient = this.clientService.updateClient(email,client);
            return new ResponseEntity<>(updatedClient, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    //Open Savings Account
    @PutMapping("/open/savings")
    public ResponseEntity<?> openSavingsAccount(@RequestBody OpenAccountDto openAccountDto)
    {
        try
        {
            this.clientService.clientOpenSavingsAccount(openAccountDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }


    //Open Check Account
    @PutMapping("/open/check")
    public ResponseEntity<?> openCheckAccount(@RequestBody OpenAccountDto openAccountDto)
    {
        try
        {
            this.clientService.clientOpenCheckAccount(openAccountDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }


}
