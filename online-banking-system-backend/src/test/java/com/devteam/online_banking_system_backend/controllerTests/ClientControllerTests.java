package com.devteam.online_banking_system_backend.controllerTests;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientLoginDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.OpenAccountDto;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "file:./.env")
@Transactional
public class ClientControllerTests
{
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ClientService clientService;

    @Autowired
    public ClientControllerTests(MockMvc mockMvc, ObjectMapper objectMapper, ClientService clientService)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.clientService = clientService;
    }


    @Test
    public void testRegister() throws Exception
    {
        ClientRegisterDto reg = util.registerDto1();
        mockMvc.perform(MockMvcRequestBuilders.post("/clients/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testLogin() throws Exception
    {
        ClientRegisterDto reg = util.registerDto1();
        this.clientService.registerClient(reg);

        ClientLoginDto login = util.loginDto1();
        mockMvc.perform(MockMvcRequestBuilders.post("/clients/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testOpenSavingsAccount() throws Exception
    {
        ClientRegisterDto reg = util.registerDto1();
        this.clientService.registerClient(reg);

        OpenAccountDto open = util.openAccountDto1();
        mockMvc.perform(MockMvcRequestBuilders.put("/clients/open/savings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(open)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testOpenCheckAccount() throws Exception
    {
        ClientRegisterDto reg = util.registerDto1();
        this.clientService.registerClient(reg);

        OpenAccountDto open = util.openAccountDto1();
        mockMvc.perform(MockMvcRequestBuilders.put("/clients/open/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(open)))
                .andExpect(status().isCreated());
    }
}
