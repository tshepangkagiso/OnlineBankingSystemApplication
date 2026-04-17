package com.devteam.online_banking_system_backend.controllerTests;

import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.CheckTransactionDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientLoginDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.services.CheckAccountService;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
@Transactional
public class CheckAccountControllerTests
{
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ClientService clientService;
    private final CheckAccountService checkAccountService;

    @Autowired
    public CheckAccountControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, ClientService clientService, CheckAccountService checkAccountService)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.clientService = clientService;
        this.checkAccountService = checkAccountService;
    }


    @Test
    public void testDeposit() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto1();
        ClientLoginDto loginDto = util.loginDto1();
        String email = util.registerDto1().getEmail();
        CheckAccount checkAccount = setup(clientRegisterDto,loginDto,email);

        //deposit
        CheckTransactionDto deposit = new CheckTransactionDto( checkAccount.getCheckAccountId(), new BigDecimal("500.00"));
        mockMvc.perform(MockMvcRequestBuilders.put("/checkaccounts/deposit/tomholland@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.00));
    }

    @Test
    public void testWithdraw() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto2();
        ClientLoginDto loginDto = util.loginDto2();
        String email = util.registerDto2().getEmail();

        CheckAccount checkAccount = setup(clientRegisterDto,loginDto,email);

        //deposit
        CheckTransactionDto deposit = new CheckTransactionDto( checkAccount.getCheckAccountId(), new BigDecimal("500.00"));
        this.checkAccountService.deposit(deposit,email);

        //withdraw
        CheckTransactionDto withdraw = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("700.00"));
        mockMvc.perform(MockMvcRequestBuilders.put("/checkaccounts/withdraw/tomholland@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(-200.00));
    }


    private CheckAccount setup(ClientRegisterDto clientRegisterDto, ClientLoginDto loginDto, String email)
    {
        // 1. Register
        clientService.registerClient(clientRegisterDto);

        // 2. Login
        clientService.login(loginDto);

        // 3. Open Check
        OpenAccountDto openAccountDto = new OpenAccountDto(email);
        clientService.clientOpenCheckAccount(openAccountDto);

        Client client  = clientService.findClientByEmail(email);
        return client.getCheckAccount();
    }
}
