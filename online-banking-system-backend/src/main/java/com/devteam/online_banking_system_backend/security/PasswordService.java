package com.devteam.online_banking_system_backend.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService
{
    private final PasswordEncoder passwordEncoder;

    public PasswordService(PasswordEncoder passwordEncoder)
    {
        this.passwordEncoder = passwordEncoder;
    }

    public String HashPassword(String rawPassword)
    {
        return this.passwordEncoder.encode(rawPassword);
    }

    public boolean isVerifiedPassword(String rawPassword, String hashedPassword)
    {
        return this.passwordEncoder.matches(rawPassword,hashedPassword);
    }
}
