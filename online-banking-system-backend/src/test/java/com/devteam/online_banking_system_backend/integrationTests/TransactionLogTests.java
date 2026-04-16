package com.devteam.online_banking_system_backend.integrationTests;

import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.CheckTransactionDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos.SavingsTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.persistence.entities.TransactionLog;
import com.devteam.online_banking_system_backend.persistence.enums.ACCOUNTTYPE;
import com.devteam.online_banking_system_backend.persistence.enums.TRANSACTIONTYPE;
import com.devteam.online_banking_system_backend.services.CheckAccountService;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.services.SavingsAccountService;
import com.devteam.online_banking_system_backend.services.TransactionLogService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "file:./.env")
@Transactional
public class TransactionLogTests {

    private final ClientService clientService;
    private final CheckAccountService checkAccountService;
    private final SavingsAccountService savingsAccountService;
    private final TransactionLogService underTests;

    @Autowired
    public TransactionLogTests(ClientService clientService, CheckAccountService checkAccountService,
                               SavingsAccountService savingsAccountService, TransactionLogService underTests) {
        this.clientService = clientService;
        this.checkAccountService = checkAccountService;
        this.savingsAccountService = savingsAccountService;
        this.underTests = underTests;
    }

    private final String testEmail = util.openAccountDto1().getEmail();

    @BeforeEach
    void setup()
    {
        // 1. Register a client
        ClientRegisterDto registerDto = util.registerDto1();
        clientService.registerClient(registerDto);

        // 2. Open both accounts
        OpenAccountDto openDto = new OpenAccountDto(testEmail);
        clientService.clientOpenCheckAccount(openDto);
        clientService.clientOpenSavingsAccount(openDto);
    }

    @Test
    @DisplayName("Should record a log when a deposit is made to Check Account")
    void shouldRecordLogOnCheckDeposit() {
        // Arrange
        Client client = clientService.findClientByEmail(testEmail);
        Long checkId = client.getCheckAccount().getCheckAccountId();
        BigDecimal depositAmount = new BigDecimal("1000.00");

        // Act
        checkAccountService.deposit(new CheckTransactionDto(checkId, depositAmount), testEmail);

        // Assert
        Iterable<TransactionLog> logs = underTests.getTransactionHistoryByEmail(testEmail);

        assertThat(logs).isNotEmpty();
        TransactionLog log = logs.iterator().next();
        assertThat(log.getAccountType()).isEqualTo(ACCOUNTTYPE.CHECKACCOUNT);
        assertThat(log.getTransactionType()).isEqualTo(TRANSACTIONTYPE.DEPOSIT);
        assertThat(log.getPostTransactionBalance()).isEqualByComparingTo(depositAmount);
    }

    @Test
    @DisplayName("Should verify audit trail contains multiple transactions in correct order")
    void shouldMaintainCorrectHistoryOrder() {
        // Arrange
        Client client = clientService.findClientByEmail(testEmail);
        Long savingsId = client.getSavingsAccount().getSavingsAccountId();

        // Act: Deposit (R500) then Withdraw (R200)
        savingsAccountService.deposit(new SavingsTransactionDto(savingsId, new BigDecimal("500.00")), testEmail);
        savingsAccountService.withdraw(new SavingsTransactionDto(savingsId, new BigDecimal("200.00")), testEmail);

        // Assert
        List<TransactionLog> logs = StreamSupport
                .stream(underTests.getTransactionHistoryByEmail(testEmail).spliterator(), false)
                .collect(Collectors.toList());

        assertThat(logs).hasSize(4);

        // Index 0 must be WITHDRAWAL because of ORDER BY TransactionDateTime DESC
        assertThat(logs.get(0).getTransactionType()).isEqualTo(TRANSACTIONTYPE.WITHDRAWAL);
        assertThat(logs.get(0).getPostTransactionBalance()).isEqualByComparingTo("300.00");

        // Index 1 is the earlier DEPOSIT
        assertThat(logs.get(1).getTransactionType()).isEqualTo(TRANSACTIONTYPE.DEPOSIT);
        assertThat(logs.get(1).getPostTransactionBalance()).isEqualByComparingTo("500.00");
    }

    @Test
    @DisplayName("Admin should be able to filter high-value transactions")
    void adminShouldFindHighValueTransactions() {
        // Arrange
        Client client = clientService.findClientByEmail(testEmail);
        Long checkId = client.getCheckAccount().getCheckAccountId();

        // Act: One small deposit, one large deposit
        checkAccountService.deposit(new CheckTransactionDto(checkId, new BigDecimal("100.00")), testEmail);
        checkAccountService.deposit(new CheckTransactionDto(checkId, new BigDecimal("15000.00")), testEmail);

        // Assert: Threshold is R10,000
        Iterable<TransactionLog> highValueLogs = underTests.findHighValueTransactions(new BigDecimal("10000.00"));

        List<TransactionLog> filteredLogs = StreamSupport
                .stream(highValueLogs.spliterator(), false)
                .collect(Collectors.toList());

        assertThat(filteredLogs).hasSize(1);
        assertThat(filteredLogs.get(0).getPostTransactionBalance()).isEqualByComparingTo("15100.00");
    }
}
