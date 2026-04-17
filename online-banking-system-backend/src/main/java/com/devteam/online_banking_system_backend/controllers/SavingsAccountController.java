package com.devteam.online_banking_system_backend.controllers;

import com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos.SavingsTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.services.SavingsAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/savingsaccounts")
public class SavingsAccountController
{
    private final SavingsAccountService savingsAccountService;

    public SavingsAccountController(SavingsAccountService savingsAccountService)
    {
        this.savingsAccountService = savingsAccountService;
    }

    //GET BY ID (READ)
    @GetMapping("/{id}")
    public ResponseEntity<SavingsAccount> getById(@PathVariable Long id)
    {
        try
        {
            SavingsAccount account = this.savingsAccountService.getSavingsAccountByID(id);
            return new ResponseEntity<>(account,HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //DEPOSIT
    @PutMapping("/deposit/{email}")
    public ResponseEntity<SavingsAccount> deposit(@RequestBody SavingsTransactionDto savingsTransactionDto, @PathVariable String email)
    {
        try
        {
            SavingsAccount account = this.savingsAccountService.deposit(savingsTransactionDto, email);
            return new ResponseEntity<>(account,HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //WITHDRAW
    @PutMapping("/withdraw/{email}")
    public ResponseEntity<SavingsAccount> withdraw(@RequestBody SavingsTransactionDto savingsTransactionDto, @PathVariable String email)
    {
        try
        {
            SavingsAccount account = this.savingsAccountService.withdraw(savingsTransactionDto, email);
            return new ResponseEntity<>(account,HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
