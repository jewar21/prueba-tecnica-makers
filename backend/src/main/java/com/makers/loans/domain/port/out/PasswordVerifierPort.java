package com.makers.loans.domain.port.out;

import java.util.Optional;

public interface PasswordVerifierPort {
    boolean matches(String rawPassword, Optional<String> storedHash);
}
