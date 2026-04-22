package com.devteam.online_banking_system_backend.security;

import com.devteam.online_banking_system_backend.persistence.dtos.clientDtos.ClientRegisterDto;
import com.devteam.online_banking_system_backend.persistence.entities.Client;
import com.devteam.online_banking_system_backend.persistence.repositories.IClientRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ClientUserDetailsService implements UserDetailsService
{
    private final IClientRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ClientUserDetailsService(IClientRepository repository, PasswordEncoder passwordEncoder)
    {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Optional<Client> client = this.repository.findByEmail(username);
        if (client.isEmpty())
            throw new UsernameNotFoundException("User not found with email: " + username);
        return new ClientUserDetails(client.get());
    }

    //Additional for registering or managing users

    public Client register(ClientRegisterDto dto)
    {
        String hashedPassword = this.passwordEncoder.encode(dto.getPassword());
        Client client = new Client(dto.getAccountHolder(),hashedPassword, dto.getEmail());
        return repository.save(client);
    }

}
