package com.devteam.online_banking_system_backend.integrationTests;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "file:./.env")
public class CheckAccountTests
{

    @Autowired
    public CheckAccountTests()
    {

    }
}
