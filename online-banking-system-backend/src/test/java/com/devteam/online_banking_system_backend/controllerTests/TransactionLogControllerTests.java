package com.devteam.online_banking_system_backend.controllerTests;

import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.CheckTransactionDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos.SavingsTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.services.CheckAccountService;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.services.SavingsAccountService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
@Transactional
public class TransactionLogControllerTests
{
    private final MockMvc mockMvc;
    private final ClientService clientService;
    private final SavingsAccountService savingsAccountService;
    private final CheckAccountService checkAccountService;

    @Autowired
    public TransactionLogControllerTests(MockMvc mockMvc, ClientService clientService,SavingsAccountService savingsAccountService,CheckAccountService checkAccountService)
    {
        this.mockMvc = mockMvc;
        this.clientService = clientService;
        this.savingsAccountService = savingsAccountService;
        this.checkAccountService = checkAccountService;
    }

    @Test
    public void testGetHistoryByAccountType() throws Exception
    {
        String email = setup();

        mockMvc.perform(get("/transactionlogs/history/" + email + "/CHECKACCOUNT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountType").value("CHECKACCOUNT"));

        mockMvc.perform(get("/transactionlogs/history/" + email + "/SAVINGSACCOUNT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountType").value("SAVINGSACCOUNT"));
    }

    @Test
    public void testGetByDateRange() throws Exception
    {
        String email = setup();

        String start = LocalDateTime.now().minusMinutes(5).toString();
        String end = LocalDateTime.now().plusMinutes(5).toString();

        mockMvc.perform(get("/transactionlogs/admin/range")
                        .param("start", start)
                        .param("end", end))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void testGetByTransactionType() throws Exception
    {
        String email = setup();

        mockMvc.perform(get("/transactionlogs/admin/type/DEPOSIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionType").value("DEPOSIT"));

        mockMvc.perform(get("/transactionlogs/admin/type/WITHDRAWAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].transactionType").value("WITHDRAWAL"));
    }

    @Test
    public void testGetHistoryByEmail() throws Exception
    {
        String email = setup();

        mockMvc.perform(get("/transactionlogs/history/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].transactionType").value("WITHDRAWAL"))
                .andExpect(jsonPath("$[5].transactionType").value("ACCOUNTOPENING"));
    }

    @Test
    public void testGetRecentTransactions() throws Exception
    {
        String email = setup();

        mockMvc.perform(get("/transactionlogs/recent/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    public void testGetHighValueTransactions() throws Exception
    {
        String email = setup();

        mockMvc.perform(get("/transactionlogs/admin/high-value")
                        .param("threshold", "100.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }


    private String setup()
    {
        ClientRegisterDto reg = util.registerDto1();
        Client client = this.clientService.registerClient(reg);

        OpenAccountDto open = util.openAccountDto1();
        this.clientService.clientOpenCheckAccount(open);
        this.clientService.clientOpenSavingsAccount(open);


        SavingsAccount savingsAccount = client.getSavingsAccount();
        CheckAccount checkAccount = client.getCheckAccount();
        String email = client.getEmail();

        //Check transactions
        CheckTransactionDto depositCheck = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("500.00"));
        this.checkAccountService.deposit(depositCheck,email);
        CheckTransactionDto withdrawCheck = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("700.00"));
        this.checkAccountService.withdraw(withdrawCheck,email);

        //Savings transactions
        SavingsTransactionDto depositSavings = new SavingsTransactionDto(savingsAccount.getSavingsAccountId(), new BigDecimal("1000.00"));
        this.savingsAccountService.deposit(depositSavings,email);
        SavingsTransactionDto withdrawSavings = new SavingsTransactionDto(savingsAccount.getSavingsAccountId(),new BigDecimal("400.00"));
        this.savingsAccountService.withdraw(withdrawSavings,email);

        return email;
    }
}

