package com.devteam.online_banking_system_backend.integrationTests;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientLoginDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientLoginResponseDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.services.ClientService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "file:./.env")
@Transactional
public class ClientServiceTests
{
    private final ClientService underTests;

    @Autowired
    public ClientServiceTests( ClientService underTests)
    {
        this.underTests = underTests;
    }

    @BeforeEach
    void setup()
    {
        ClientRegisterDto dto1 = util.registerDto1();
        ClientRegisterDto dto2 = util.registerDto2();
        underTests.registerClient(dto1);
        underTests.registerClient(dto2);
    }

    @Test
    public void testGetClientByEmail() {
        String email = "tomholland@gmail.com";
        Client client = underTests.findClientByEmail(email);

        assertThat(client.getEmail()).isEqualTo(email);
    }

    @Test void testGetAllClients()
    {
        List<Client> results = underTests.getAllClients();
        assertThat(results).hasSize(2);
    }

    @Test
    public void testClientCanBeRegistered()
    {
        ClientRegisterDto newClient = util.registerDto3();
        Client registeredClient = underTests.registerClient(newClient);

        assertThat(registeredClient.getEmail()).isEqualTo(newClient.getEmail());
    }

    @Test
    public void testClientCanLogin()
    {
        ClientLoginDto loginDto = util.loginDto2();

        ClientLoginResponseDto responseDto = underTests.login(loginDto);
        Client client = underTests.findClientByEmail(loginDto.getEmail());

        assertThat(responseDto.getEmail()).isEqualTo(loginDto.getEmail());
        assertThat(client.getEmail()).isEqualTo(loginDto.getEmail());

        assertThat(responseDto.getEmail()).isEqualTo(client.getEmail());
        assertThat(responseDto.getAccountNumber()).isEqualTo(client.getAccountNumber());
        assertThat(responseDto.getAccountHolder()).isEqualTo(client.getAccountHolder());
    }

    @Test
    public void testClientCanOpenSavingsAccount()
    {
        // 1. Arrange: Use an existing client (registered in @BeforeEach)
        String email = util.openAccountDto1().getEmail();
        Client clientBefore = underTests.findClientByEmail(email);

        // Safety Check: Verify it starts as null
        assertThat(clientBefore.getSavingsAccount()).isNull();

        // 2. Act: Call the void method
        underTests.clientOpenSavingsAccount(util.openAccountDto1());

        // 3. Assert: Retrieve a FRESH copy of the client from the DB
        Client clientAfter = underTests.findClientByEmail(email);

        assertThat(clientAfter.getSavingsAccount()).isNotNull();
        assertThat(clientAfter.getSavingsAccount().getBalance()).isEqualTo(0.0);
    }

    @Test
    public void testClientCanOpenCheckAccount()
    {
        // 1. Arrange: Use an existing client (registered in @BeforeEach)
        String email = util.openAccountDto2().getEmail();
        Client clientBefore = underTests.findClientByEmail(email);

        // Safety Check: Verify it starts as null
        assertThat(clientBefore.getCheckAccount()).isNull();

        // 2. Act: Call the void method
        underTests.clientOpenCheckAccount(util.openAccountDto2());

        // 3. Assert: Retrieve a FRESH copy of the client from the DB
        Client clientAfter = underTests.findClientByEmail(email);

        assertThat(clientAfter.getCheckAccount()).isNotNull();
        assertThat(clientAfter.getCheckAccount().getBalance()).isEqualTo(0.0);
    }

}
