package com.devteam.online_banking_system_backend.controllerTests;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.utility.util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
public class ClientControllerTests
{
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public ClientControllerTests(MockMvc mockMvc, ObjectMapper objectMapper)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void testRegisterClientRoute() throws Exception
    {
        ClientRegisterDto clientRegisterDto = util.registerDto1();
        String clientRegisterDtoJson = objectMapper.writeValueAsString(clientRegisterDto);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/clients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientRegisterDtoJson)
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.email").value(clientRegisterDto.getEmail()));
    }

}
