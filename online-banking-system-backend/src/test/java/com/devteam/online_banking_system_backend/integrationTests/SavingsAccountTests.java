package com.devteam.online_banking_system_backend.integrationTests;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos.SavingsTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.services.SavingsAccountService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "file:./.env")
@Transactional
public class SavingsAccountTests
{
    private final SavingsAccountService underTests;
    private final ClientService clientService;
    private SavingsAccount savingsAccount;

    @Autowired
    public SavingsAccountTests(SavingsAccountService underTests, ClientService clientService)
    {
        this.underTests = underTests;
        this.clientService = clientService;
    }

    private final String email = util.registerDto1().getEmail();
    @BeforeEach
    void setup()
    {
        ClientRegisterDto dto = util.registerDto1();
        clientService.registerClient(dto);

        OpenAccountDto openAccountDto = util.openAccountDto1();
        clientService.clientOpenSavingsAccount(openAccountDto);

        Client savingsAccountClient = clientService.findClientByEmail(util.openAccountDto1().getEmail());
        savingsAccount = savingsAccountClient.getSavingsAccount();
    }

    @Test
    public void testGetSavingsAccountByIdWorks()
    {
        SavingsAccount foundSavingsAccount = underTests.getSavingsAccountByID(savingsAccount.getSavingsAccountId());
        assertThat(foundSavingsAccount.getSavingsAccountId()).isEqualTo(savingsAccount.getSavingsAccountId());
        assertThat(foundSavingsAccount).isEqualTo(savingsAccount);
    }

    @Test
    public void testDepositingIntoSavingsAccount()
    {
        SavingsAccount foundSavingsAccount = underTests.getSavingsAccountByID(savingsAccount.getSavingsAccountId());
        SavingsTransactionDto dto = new SavingsTransactionDto(foundSavingsAccount.getSavingsAccountId(), new BigDecimal("100.0"));

        SavingsAccount account = underTests.deposit(dto,email);
        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("100.0"));

    }

    @Test
    public void testWithdrawingOutOfSavingsAccount()
    {
        SavingsAccount foundSavingsAccount = underTests.getSavingsAccountByID(savingsAccount.getSavingsAccountId());
        SavingsTransactionDto dto1 = new SavingsTransactionDto(foundSavingsAccount.getSavingsAccountId(), new BigDecimal("100.0"));
        underTests.deposit(dto1,email);

        SavingsTransactionDto dto2 = new SavingsTransactionDto(foundSavingsAccount.getSavingsAccountId(), new BigDecimal("100.0"));
        SavingsAccount account = underTests.withdraw(dto2,email);

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void testingApplyingInterestToSavingsAccount()
    {
        SavingsAccount foundSavingsAccount = underTests.getSavingsAccountByID(savingsAccount.getSavingsAccountId());
        SavingsTransactionDto dto1 = new SavingsTransactionDto(foundSavingsAccount.getSavingsAccountId(), new BigDecimal("100.0"));
        SavingsAccount account = underTests.deposit(dto1,email);

        account.ApplyInterest();

        BigDecimal interestRate = new BigDecimal("0.035").divide(new BigDecimal("31"), 10, RoundingMode.HALF_UP);
        BigDecimal interest = new BigDecimal("100.0").multiply(interestRate);
        BigDecimal expectedAmount = new BigDecimal("100.0").add(interest).setScale(2, RoundingMode.HALF_EVEN);

        assertThat(account.getBalance()).isEqualByComparingTo(expectedAmount);
    }
}