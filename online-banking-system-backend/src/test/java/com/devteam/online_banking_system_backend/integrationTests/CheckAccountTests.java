package com.devteam.online_banking_system_backend.integrationTests;

import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.CheckTransactionDto;
import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.OverdraftToggleDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.services.CheckAccountService;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "file:./.env")
@Transactional
public class CheckAccountTests
{

    private final CheckAccountService underTests;
    private final ClientService clientService;
    private CheckAccount checkAccount;

    @Autowired
    public CheckAccountTests(CheckAccountService underTests, ClientService clientService )
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
        clientService.clientOpenCheckAccount(openAccountDto);

        Client savingsAccountClient = clientService.findClientByEmail(util.openAccountDto1().getEmail());
        checkAccount = savingsAccountClient.getCheckAccount();
    }


    @Test
    @Order(1)
    public void testGetCheckAccountById()
    {
        CheckAccount foundCheckAccount = underTests.getCheckAccountById(checkAccount.getCheckAccountId());
        assertThat(foundCheckAccount.getCheckAccountId()).isEqualTo(checkAccount.getCheckAccountId());
        assertThat(foundCheckAccount).isEqualTo(checkAccount);
    }

    @Test
    @Order(2)
    public void testDepositingIntoCheckAccount()
    {
        CheckAccount foundCheckAccount = underTests.getCheckAccountById(checkAccount.getCheckAccountId());

        CheckTransactionDto dto = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("1000.0"));
        CheckAccount account = underTests.deposit(dto,email);

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("1000.0"));
    }

    @Test
    @Order(3)
    public void testWithdrawingOutOfCheckAccount()
    {
        CheckAccount foundCheckAccount = underTests.getCheckAccountById(checkAccount.getCheckAccountId());

        CheckTransactionDto dto1 = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("1000.0"));
        underTests.deposit(dto1,email);

        CheckTransactionDto dto2 = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("1300.0"));
        CheckAccount account = underTests.withdraw(dto2,email);

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("-300.0"));
    }


    @Test
    @Order(4)
    public void testOverdraftLimitSetter()
    {
        //set new overdraft limit and test its correct
        CheckAccount foundCheckAccount = underTests.getCheckAccountById(checkAccount.getCheckAccountId());
        CheckTransactionDto dto1 = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("1000.0"));
        underTests.OverdraftLimitSetter(dto1, email);
        assertThat(foundCheckAccount.getOverdraftLimit()).isEqualByComparingTo(dto1.getAmount());


        //test that limit by overdrafting a withdrawal
        CheckTransactionDto dto2 = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("1000.0"));
        underTests.deposit(dto2,email);

        CheckTransactionDto dto3 = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("2000.0"));
        CheckAccount account = underTests.withdraw(dto3,email);

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("-1000.0"));
    }

    @Test
    @Order(5)
    public void testOverdraftToggle()
    {
        //toggle overdraft and check its zero
        CheckAccount foundCheckAccount = underTests.getCheckAccountById(checkAccount.getCheckAccountId());
        OverdraftToggleDto overdraftToggleDto = new OverdraftToggleDto(foundCheckAccount.getCheckAccountId(), false);
        underTests.OverdraftToggle(overdraftToggleDto, email);
        assertThat(foundCheckAccount.getOverdraftLimit()).isEqualByComparingTo(BigDecimal.ZERO);

        //toggle overdraft and check it returns to base
        overdraftToggleDto.setToggle(true);
        underTests.OverdraftToggle(overdraftToggleDto, email);

        CheckTransactionDto dto1 = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("1000.0"));
        underTests.deposit(dto1,email);

        CheckTransactionDto dto2 = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("1300.0"));
        CheckAccount account = underTests.withdraw(dto2,email);

        assertThat(account.getBalance()).isEqualByComparingTo(new BigDecimal("-300.0"));
    }

}
