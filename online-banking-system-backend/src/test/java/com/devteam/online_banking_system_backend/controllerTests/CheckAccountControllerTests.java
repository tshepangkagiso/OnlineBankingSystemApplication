package com.devteam.online_banking_system_backend.controllerTests;

import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.CheckTransactionDto;
import com.devteam.online_banking_system_backend.persistence.dtos.checkAccountDtos.OverdraftToggleDto;
import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthRequestDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.security.ClientUserDetailsService;
import com.devteam.online_banking_system_backend.security.JwtService;
import com.devteam.online_banking_system_backend.services.CheckAccountService;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
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
    private final ClientUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Autowired
    public CheckAccountControllerTests(MockMvc mockMvc,JwtService jwtService,ClientUserDetailsService userDetailsService ,ObjectMapper objectMapper, ClientService clientService, CheckAccountService checkAccountService)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.clientService = clientService;
        this.checkAccountService = checkAccountService;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }


    @Test
    public void testDeposit() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto1();
        AuthRequestDto loginDto = util.loginDto1();
        String email = util.registerDto1().getEmail();
        CheckAccount checkAccount = setup(clientRegisterDto,loginDto,email);
        String token = jwtService.generateToken(email);

        //deposit
        CheckTransactionDto deposit = new CheckTransactionDto( checkAccount.getCheckAccountId(), new BigDecimal("500.00"));
        mockMvc.perform(MockMvcRequestBuilders.put("/checkaccounts/deposit/tomholland@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(deposit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.00));
    }

    @Test
    public void testWithdraw() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto2();
        AuthRequestDto loginDto = util.loginDto2();
        String email = util.registerDto2().getEmail();
        String token = jwtService.generateToken(email);
        CheckAccount checkAccount = setup(clientRegisterDto,loginDto,email);

        //deposit
        CheckTransactionDto deposit = new CheckTransactionDto( checkAccount.getCheckAccountId(), new BigDecimal("500.00"));
        this.checkAccountService.deposit(deposit,email);

        //withdraw
        CheckTransactionDto withdraw = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("700.00"));
        mockMvc.perform(MockMvcRequestBuilders.put("/checkaccounts/withdraw/tomholland@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(-200.00));
    }

    @Test
    public void testOverdraftLimitSetter() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto2();
        AuthRequestDto loginDto = util.loginDto2();
        String email = util.registerDto2().getEmail();
        String token = jwtService.generateToken(email);
        CheckAccount checkAccount = setup(clientRegisterDto,loginDto,email);

        //set new overdraftLimit
        CheckTransactionDto limit = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("1000.0"));
        mockMvc.perform(
                MockMvcRequestBuilders.put("/checkaccounts/overdraft/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(limit))
        ).andExpect(status().isOk()).andExpect(jsonPath("$.overdraftLimit").value(new BigDecimal("1000.0")));


        //deposit
        CheckTransactionDto deposit = new CheckTransactionDto( checkAccount.getCheckAccountId(), new BigDecimal("1000.00"));
        this.checkAccountService.deposit(deposit,email);

        //withdraw
        CheckTransactionDto withdraw = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("2000.00"));
        mockMvc.perform(MockMvcRequestBuilders.put("/checkaccounts/withdraw/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(-1000.00));
    }

    @Test
    public void testOverdraftToggle() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto2();
        AuthRequestDto loginDto = util.loginDto2();
        String email = util.registerDto2().getEmail();
        String token = jwtService.generateToken(email);
        CheckAccount checkAccount = setup(clientRegisterDto,loginDto,email);

        //toggle off
        OverdraftToggleDto overdraftToggleDtoFalse = new OverdraftToggleDto(checkAccount.getCheckAccountId(),false);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/checkaccounts/toggle/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(overdraftToggleDtoFalse))
        ).andExpect(status().isOk()).andExpect(jsonPath("$.overdraftLimit").value(0));

        //toggle on
        OverdraftToggleDto overdraftToggleDtoTrue = new OverdraftToggleDto(checkAccount.getCheckAccountId(),true);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/checkaccounts/toggle/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(overdraftToggleDtoTrue))
        ).andExpect(status().isOk()).andExpect(jsonPath("$.overdraftLimit").value(300));

        //deposit
        CheckTransactionDto deposit = new CheckTransactionDto( checkAccount.getCheckAccountId(), new BigDecimal("500.00"));
        this.checkAccountService.deposit(deposit,email);

        //withdraw
        CheckTransactionDto withdraw = new CheckTransactionDto(checkAccount.getCheckAccountId(), new BigDecimal("700.00"));
        mockMvc.perform(MockMvcRequestBuilders.put("/checkaccounts/withdraw/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(withdraw)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(-200.00));
    }


    private CheckAccount setup(ClientRegisterDto clientRegisterDto, AuthRequestDto loginDto, String email)
    {
        // 1. Register
        this.userDetailsService.register(clientRegisterDto);

        // 2. Open Check
        OpenAccountDto openAccountDto = new OpenAccountDto(email);
        clientService.clientOpenCheckAccount(openAccountDto);

        Client client  = clientService.findClientByEmail(email);
        return client.getCheckAccount();
    }
}
