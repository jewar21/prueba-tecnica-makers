package com.makers.loans.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(String secret, Duration expiration) {

    public JwtProperties {
        if (secret == null || secret.getBytes(java.nio.charset.StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("security.jwt.secret must contain at least 32 bytes");
        }
        if (expiration == null || expiration.isZero() || expiration.isNegative()) {
            throw new IllegalArgumentException("security.jwt.expiration must be positive");
        }
    }
}
