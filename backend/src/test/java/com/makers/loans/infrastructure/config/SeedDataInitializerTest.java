package com.makers.loans.infrastructure.config;

import com.makers.loans.domain.model.Role;
import com.makers.loans.infrastructure.persistence.entity.UserJpaEntity;
import com.makers.loans.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SeedDataInitializerTest {

    @Test
    void createsUserAndAdminWithEncodedPasswordsWhenConfigured() throws Exception {
        UserJpaRepository users = mock(UserJpaRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode("user-password")).thenReturn("user-hash");
        when(passwordEncoder.encode("admin-password")).thenReturn("admin-hash");

        SeedDataInitializer initializer = new SeedDataInitializer(
                users,
                passwordEncoder,
                "user@makers.com",
                "user-password",
                "admin@makers.com",
                "admin-password"
        );

        initializer.run(null);

        ArgumentCaptor<UserJpaEntity> captor = ArgumentCaptor.forClass(UserJpaEntity.class);
        verify(users, org.mockito.Mockito.times(2)).save(captor.capture());
        assertThat(captor.getAllValues())
                .extracting(UserJpaEntity::getEmail, UserJpaEntity::getRole, UserJpaEntity::getPasswordHash)
                .containsExactlyInAnyOrder(
                        org.assertj.core.groups.Tuple.tuple("user@makers.com", Role.USER, "user-hash"),
                        org.assertj.core.groups.Tuple.tuple("admin@makers.com", Role.ADMIN, "admin-hash")
                );
    }

    @Test
    void rejectsEqualUserAndAdminEmails() {
        UserJpaRepository users = mock(UserJpaRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        SeedDataInitializer initializer = new SeedDataInitializer(
                users,
                passwordEncoder,
                "same@makers.com",
                "user-password",
                " SAME@makers.com ",
                "admin-password"
        );

        assertThatThrownBy(() -> initializer.run(null))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Seed USER and ADMIN emails must be different");
    }
}
