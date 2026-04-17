package com.devteam.online_banking_system_backend.controllerTests;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
public class CheckAccountControllerTests
{
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public CheckAccountControllerTests(MockMvc mockMvc, ObjectMapper objectMapper)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }
}
