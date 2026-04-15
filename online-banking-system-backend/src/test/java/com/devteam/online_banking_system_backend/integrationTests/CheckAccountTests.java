package com.devteam.online_banking_system_backend.integrationTests;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
import com.devteam.online_banking_system_backend.persistence.entities.CheckAccount;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.services.CheckAccountService;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
    public CheckAccountTests(CheckAccountService underTests,ClientService clientService )
    {
        this.underTests = underTests;
        this.clientService = clientService;
    }

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
}
