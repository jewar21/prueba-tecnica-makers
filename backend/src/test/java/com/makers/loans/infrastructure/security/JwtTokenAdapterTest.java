package com.makers.loans.infrastructure.security;

import com.makers.loans.domain.model.Role;
import com.makers.loans.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenAdapterTest {

    @Test
    void issuesSignedTokenWithSubjectRoleAndExpiration() {
        SecretKey key = new SecretKeySpec(
                "test-only-secret-key-32-characters".getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
        JwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        Instant now = Instant.parse("2026-01-01T12:00:00Z");
        Clock fixedClock = Clock.fixed(now, ZoneOffset.UTC);
        JwtTimestampValidator timestampValidator = new JwtTimestampValidator(Duration.ZERO);
        timestampValidator.setClock(fixedClock);
        decoder.setJwtValidator(timestampValidator);
        JwtTokenAdapter adapter = new JwtTokenAdapter(
                encoder,
                Duration.ofMinutes(30),
                fixedClock
        );
        User user = new User(1L, "admin@makers.com", "hash", Role.ADMIN, true, now);

        String token = adapter.issue(user);
        Jwt jwt = decoder.decode(token);

        assertThat(jwt.getSubject()).isEqualTo("admin@makers.com");
        assertThat(jwt.getClaimAsStringList("roles")).isEqualTo(List.of("ADMIN"));
        assertThat(jwt.getIssuedAt()).isEqualTo(now);
        assertThat(jwt.getExpiresAt()).isEqualTo(now.plus(Duration.ofMinutes(30)));
    }
}
