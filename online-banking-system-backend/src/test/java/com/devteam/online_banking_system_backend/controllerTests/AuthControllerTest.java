package com.devteam.online_banking_system_backend.controllerTests;

import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthRequestDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.security.ClientUserDetailsService;
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
public class AuthControllerTest
{
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ClientUserDetailsService userDetailsService;

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, ClientUserDetailsService userDetailsService)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userDetailsService = userDetailsService;
    }

    @Test
    public void testRegister() throws Exception
    {
        ClientRegisterDto reg = util.registerDto1();
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testLogin() throws Exception
    {
        ClientRegisterDto reg = util.registerDto1();
        this.userDetailsService.register(reg);

        AuthRequestDto login = util.loginDto1();
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
}
