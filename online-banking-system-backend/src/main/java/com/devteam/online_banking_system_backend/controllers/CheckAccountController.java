package com.devteam.online_banking_system_backend.controllers;

import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.CheckTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.services.CheckAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkaccounts")
public class CheckAccountController
{
    private final CheckAccountService checkAccountService;

    public CheckAccountController(CheckAccountService checkAccountService)
    {
        this.checkAccountService = checkAccountService;
    }

    //get by id (READ)
    @GetMapping("/{id}")
    public ResponseEntity<CheckAccount> getById(@PathVariable Long id)
    {
        try
        {
            CheckAccount account = this.checkAccountService.getCheckAccountById(id);
            return new ResponseEntity<>(account,HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //deposit
    @PutMapping("/deposit/{email}")
    public ResponseEntity<CheckAccount> deposit(@RequestBody CheckTransactionDto checkTransactionDto, @PathVariable String email)
    {
        try
        {
            CheckAccount account = this.checkAccountService.deposit(checkTransactionDto, email);
            return new ResponseEntity<>(account,HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //withdraw
    @PutMapping("/withdraw/{email}")
    public ResponseEntity<CheckAccount> withdraw(@RequestBody CheckTransactionDto checkTransactionDto, @PathVariable String email)
    {
        try
        {
            CheckAccount account = this.checkAccountService.withdraw(checkTransactionDto, email);
            return new ResponseEntity<>(account,HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
