package com.makers.loans.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BCryptPasswordVerifierAdapterTest {

    @Test
    void verifiesRawPasswordAgainstBcryptHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("correct-password");
        BCryptPasswordVerifierAdapter adapter = new BCryptPasswordVerifierAdapter(encoder);

        assertThat(adapter.matches("correct-password", Optional.of(hash))).isTrue();
        assertThat(adapter.matches("wrong-password", Optional.of(hash))).isFalse();
        assertThat(adapter.matches("any-password", Optional.empty())).isFalse();
    }
}
