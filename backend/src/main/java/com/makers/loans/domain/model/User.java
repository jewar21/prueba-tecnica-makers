package com.makers.loans.domain.model;

import java.time.Instant;

public record User(
        Long id,
        String email,
        String passwordHash,
        Role role,
        boolean enabled,
        Instant createdAt
) {
}
