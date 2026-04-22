package com.devteam.online_banking_system_backend.integrationTests;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.security.ClientUserDetailsService;
import com.devteam.online_banking_system_backend.utility.util;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "file:./.env")
@Transactional
public class AuthIntegrationTest
{
    private final ClientUserDetailsService underTests;

    @Autowired
    public AuthIntegrationTest(ClientUserDetailsService underTests)
    {
        this.underTests = underTests;
    }

    @Test
    public void testClientCanBeRegistered()
    {
        ClientRegisterDto newClient = util.registerDto3();
        Client registeredClient = this.underTests.register(newClient);

        assertThat(registeredClient.getEmail()).isEqualTo(newClient.getEmail());
    }
}
