package com.devteam.online_banking_system_backend.backgroundScheduling;

import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.services.TransactionLogService;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class backgroundschedular
{
    private final ClientService clientService;
    private final TransactionLogService transactionLogService;

    public backgroundschedular(ClientService clientService, TransactionLogService transactionLogService)
    {
        this.clientService = clientService;
        this.transactionLogService = transactionLogService;
    }

    @Scheduled(cron = "0 0 22 * * *") // Runs 10 PM daily
    @Transactional
    public void applyDailyInterest()
    {
        LocalDate today = LocalDate.now();

        List<Client> targets = clientService.getAllClients().stream()
                .filter(c -> {
                    SavingsAccount account = c.getSavingsAccount();

                    // 1. Must have money
                    if (account.getBalance().compareTo(BigDecimal.ZERO) <= 0) return false;

                    // 2. Initial "Waiting Period": Has it been a month since their first deposit?
                    LocalDate oneMonthAfterDeposit = account.getLatestDepositDate().plusMonths(1);

                    return today.isAfter(oneMonthAfterDeposit) || today.isEqual(oneMonthAfterDeposit);
                })
                .toList();

        for (Client client : targets)
        {
            Client lockedClient = clientService.findClientByEmailWRITE(client.getEmail());

            BigDecimal preBalance = lockedClient.getSavingsAccount().getBalance();
            lockedClient.getSavingsAccount().ApplyInterest();
            BigDecimal postBalance = lockedClient.getSavingsAccount().getBalance();

            clientService.updateClient(lockedClient.getEmail(),lockedClient);

            transactionLogService.recordInterestEntry(new TransactionLog(
                    null,
                    LocalDateTime.now(),
                    lockedClient.getEmail(),
                    ACCOUNTTYPE.SAVINGSACCOUNT,
                    TRANSACTIONTYPE.APPLIEDINTERESTTOSAVINGSACCOUNT,
                    preBalance,
                    postBalance
            ));
        }
    }
}
