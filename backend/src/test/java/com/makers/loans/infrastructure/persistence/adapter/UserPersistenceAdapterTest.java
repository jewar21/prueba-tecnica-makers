package com.makers.loans.infrastructure.persistence.adapter;

import com.makers.loans.domain.model.Role;
import com.makers.loans.domain.model.User;
import com.makers.loans.infrastructure.persistence.entity.UserJpaEntity;
import com.makers.loans.infrastructure.persistence.repository.UserJpaRepository;
import jakarta.persistence.Column;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserPersistenceAdapterTest {

    @Autowired
    private UserJpaRepository repository;

    @Autowired
    private UserPersistenceAdapter adapter;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void loadsExistingUserByNormalizedEmail() {
        UserJpaEntity entity = new UserJpaEntity(
                null,
                "user@makers.com",
                "hashed-password",
                Role.USER,
                true,
                Instant.parse("2026-01-01T00:00:00Z")
        );
        repository.save(entity);

        Optional<User> result = adapter.loadByEmail(" USER@MAKERS.COM ");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().email()).isEqualTo("user@makers.com");
        assertThat(result.orElseThrow().role()).isEqualTo(Role.USER);
        assertThat(result.orElseThrow().enabled()).isTrue();
    }

    @Test
    void returnsEmptyWhenEmailDoesNotExist() {
        assertThat(adapter.loadByEmail("missing@makers.com")).isEmpty();
    }

    @Test
    void keepsJpaEmailLengthAlignedWithMigration() throws NoSuchFieldException {
        Column column = UserJpaEntity.class.getDeclaredField("email").getAnnotation(Column.class);

        assertThat(column.length()).isEqualTo(254);
    }
}
