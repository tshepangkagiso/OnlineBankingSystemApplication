package com.devteam.online_banking_system_backend.controllers;

import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthRequestDto;
import com.devteam.online_banking_system_backend.persistence.dtos.AuthDtos.AuthResponseDto;
import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.security.ClientUserDetailsService;
import com.devteam.online_banking_system_backend.security.JwtService;
import com.devteam.online_banking_system_backend.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final ClientUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClientService clientService;

    public AuthController(ClientUserDetailsService userDetailsService, JwtService jwtService, AuthenticationManager authenticationManager,ClientService clientService)
    {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.clientService = clientService;
    }


    //Register a client
    @PostMapping("/register")
    public ResponseEntity<Client> registerClient(@RequestBody ClientRegisterDto clientRegisterDto)
    {
        try
        {
            Client newClient = this.userDetailsService.register(clientRegisterDto);
            return new ResponseEntity<>(newClient, HttpStatus.CREATED);
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginClient(@RequestBody AuthRequestDto authRequest)
    {
        try
        {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getEmail());
                Client client = this.clientService.findClientByEmail(authRequest.getEmail());
                AuthResponseDto response = new AuthResponseDto(client.getAccountNumber(), client.getAccountHolder(), client.getEmail(), token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        catch (RuntimeException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
