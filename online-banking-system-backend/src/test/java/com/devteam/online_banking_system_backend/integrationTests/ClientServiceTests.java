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

import java.math.BigDecimal;
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
    public void testClientCanBeUpdated()
    {
        String email = "tomholland@gmail.com";
        Client client = underTests.findClientByEmail(email);
        client.setAccountHolder("New Name For You");
        Client updatedClient = underTests.updateClient(email,client);

        assertThat(updatedClient.getAccountHolder()).isEqualTo(client.getAccountHolder());
        assertThat(updatedClient.getAccountNumber()).isEqualTo(client.getAccountNumber());
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
        String email = util.openAccountDto1().getEmail();
        Client clientBefore = underTests.findClientByEmail(email);

        assertThat(clientBefore.getSavingsAccount()).isNull();

        underTests.clientOpenSavingsAccount(util.openAccountDto1());

        Client clientAfter = underTests.findClientByEmail(email);

        assertThat(clientAfter.getSavingsAccount()).isNotNull();
        assertThat(clientAfter.getSavingsAccount().getBalance()).isEqualTo(new BigDecimal("0"));
    }

    @Test
    public void testClientCanOpenCheckAccount()
    {
        String email = util.openAccountDto2().getEmail();
        Client clientBefore = underTests.findClientByEmail(email);

        assertThat(clientBefore.getCheckAccount()).isNull();

        underTests.clientOpenCheckAccount(util.openAccountDto2());

        Client clientAfter = underTests.findClientByEmail(email);

        assertThat(clientAfter.getCheckAccount()).isNotNull();
        assertThat(clientAfter.getCheckAccount().getBalance()).isEqualTo(new BigDecimal("0"));
    }

}
