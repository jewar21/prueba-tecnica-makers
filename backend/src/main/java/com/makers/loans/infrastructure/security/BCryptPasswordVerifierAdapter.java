package com.makers.loans.infrastructure.security;

import com.makers.loans.domain.port.out.PasswordVerifierPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BCryptPasswordVerifierAdapter implements PasswordVerifierPort {

    private static final String DUMMY_PASSWORD = "authentication-timing-equalizer";

    private final PasswordEncoder passwordEncoder;
    private final String dummyHash;

    public BCryptPasswordVerifierAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.dummyHash = passwordEncoder.encode(DUMMY_PASSWORD);
    }

    @Override
    public boolean matches(String rawPassword, Optional<String> storedHash) {
        boolean matches = passwordEncoder.matches(rawPassword, storedHash.orElse(dummyHash));
        return storedHash.isPresent() && matches;
    }
}
