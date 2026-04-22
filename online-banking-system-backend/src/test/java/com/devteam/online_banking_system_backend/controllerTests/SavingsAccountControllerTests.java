package com.devteam.online_banking_system_backend.controllerTests;

import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthRequestDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.dtos.savingsAccountDtos.SavingsTransactionDto;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.persistence.entities.SavingsAccount;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.services.SavingsAccountService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
@Transactional
public class SavingsAccountControllerTests
{
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ClientService clientService;
    private final SavingsAccountService savingsAccountService;

    @Autowired
    public SavingsAccountControllerTests(MockMvc mockMvc, ObjectMapper objectMapper,ClientService clientService, SavingsAccountService savingsAccountService)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.clientService = clientService;
        this.savingsAccountService = savingsAccountService;
    }

    @Test
    public void testDeposit() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto1();
        AuthRequestDto loginDto = util.loginDto1();
        String email = util.registerDto1().getEmail();
        SavingsAccount savingsAccount = setup(clientRegisterDto,loginDto,email);

        //deposit
        BigDecimal depositAmount = new BigDecimal("1000.00");
        SavingsTransactionDto depositDto = new SavingsTransactionDto(savingsAccount.getSavingsAccountId(), depositAmount);

        mockMvc.perform(put("/savingsaccounts/deposit/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000.00));
    }

    @Test
    public void testWithdraw() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto2();
        AuthRequestDto loginDto = util.loginDto2();
        String email = util.registerDto2().getEmail();
        SavingsAccount savingsAccount = setup(clientRegisterDto,loginDto,email);


        //deposit
        BigDecimal depositAmount = new BigDecimal("1000.00");
        SavingsTransactionDto depositDto = new SavingsTransactionDto(savingsAccount.getSavingsAccountId(), depositAmount);
        savingsAccountService.deposit(depositDto,email);

        //withdraw
        BigDecimal withdrawAmount = new BigDecimal("400.00");
        SavingsTransactionDto withdrawDto = new SavingsTransactionDto(savingsAccount.getSavingsAccountId(), withdrawAmount);

        mockMvc.perform(put("/savingsaccounts/withdraw/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(600.00));
    }



    private SavingsAccount setup(ClientRegisterDto clientRegisterDto, AuthRequestDto loginDto, String email)
    {
        // 1. Register
        clientService.registerClient(clientRegisterDto);

        // 2. Login
        clientService.login(loginDto);

        // 3. Open Check
        OpenAccountDto openAccountDto = new OpenAccountDto(email);
        clientService.clientOpenSavingsAccount(openAccountDto);

        Client client  = clientService.findClientByEmail(email);
        return client.getSavingsAccount();
    }
}
