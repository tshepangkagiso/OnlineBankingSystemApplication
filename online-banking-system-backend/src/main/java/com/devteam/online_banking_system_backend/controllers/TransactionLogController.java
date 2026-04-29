package com.devteam.online_banking_system_backend.controllers;

import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import com.devteam.online_banking_system_backend.services.TransactionLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/transactionlogs")
public class TransactionLogController
{
    private final TransactionLogService transactionLogService;

    public TransactionLogController(TransactionLogService transactionLogService)
    {
        this.transactionLogService = transactionLogService;
    }

    //---- client facing ----
    // Get full history by email
    @GetMapping("/history/{email}")
    public ResponseEntity<?> getHistoryByEmail(@PathVariable String email)
    {
        try
        {
            Iterable<TransactionLog> logs = this.transactionLogService.getTransactionHistoryByEmail(email);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Get history filtered by account type (CHECKACCOUNT / SAVINGSACCOUNT)
    @GetMapping("/history/{email}/{type}")
    public ResponseEntity<?> getHistoryByAccountType(@PathVariable String email, @PathVariable ACCOUNTTYPE type)
    {
        try
        {
            Iterable<TransactionLog> logs = this.transactionLogService.getHistoryByAccountType(email, type);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Get recent transactions (e.g., top 5 or 10)
    @GetMapping("/recent/{email}")
    public ResponseEntity<?> getRecentTransactions(@PathVariable String email)
    {
        try
        {
            Iterable<TransactionLog> logs = this.transactionLogService.getRecentTransactions(email);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }




    //---- admin facing ----
    // Find transactions within a specific date range
    @GetMapping("/admin/range")
    public ResponseEntity<?> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end)
    {
        try
        {
            Iterable<TransactionLog> logs = this.transactionLogService.findTransactionsByDateRange(start, end);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Find transactions by type (DEPOSIT, WITHDRAWAL, etc.)
    @GetMapping("/admin/type/{type}")
    public ResponseEntity<?> getByTransactionType(@PathVariable TRANSACTIONTYPE type)
    {
        try
        {
            Iterable<TransactionLog> logs = this.transactionLogService.findTransactionsByType(type);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Find transactions exceeding a specific amount
    @GetMapping("/admin/high-value")
    public ResponseEntity<?> getHighValueTransactions(@RequestParam BigDecimal threshold)
    {
        try
        {
            Iterable<TransactionLog> logs = this.transactionLogService.findHighValueTransactions(threshold);
            return new ResponseEntity<>(logs, HttpStatus.OK);
        }
        catch (RuntimeException e)
        {
            Map<String,String> error = Map.of("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

}
