package com.makers.loans.infrastructure.persistence.adapter;

import com.makers.loans.domain.model.Loan;
import com.makers.loans.domain.model.LoanStatus;
import com.makers.loans.domain.model.Role;
import com.makers.loans.infrastructure.persistence.entity.UserJpaEntity;
import com.makers.loans.infrastructure.persistence.repository.LoanJpaRepository;
import com.makers.loans.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LoanPersistenceAdapterTest {

    @Autowired
    private LoanJpaRepository loanRepository;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private LoanPersistenceAdapter adapter;

    @BeforeEach
    void cleanDatabase() {
        loanRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void savesAndMapsPendingLoan() {
        Long userId = saveUser("user@makers.com");
        Instant now = Instant.parse("2026-07-23T18:00:00Z");

        Loan saved = adapter.save(Loan.create(userId, new BigDecimal("850000.00"), 18, now));

        assertThat(saved.id()).isNotNull();
        assertThat(saved.userId()).isEqualTo(userId);
        assertThat(saved.amount()).isEqualByComparingTo("850000.00");
        assertThat(saved.status()).isEqualTo(LoanStatus.PENDING);
        assertThat(saved.createdAt()).isEqualTo(now);
    }

    @Test
    void findsOnlyRequestedUsersLoansNewestFirst() {
        Long firstUser = saveUser("first@makers.com");
        Long secondUser = saveUser("second@makers.com");
        adapter.save(Loan.create(
                firstUser,
                new BigDecimal("1000.00"),
                12,
                Instant.parse("2026-07-20T10:00:00Z")
        ));
        adapter.save(Loan.create(
                firstUser,
                new BigDecimal("2000.00"),
                24,
                Instant.parse("2026-07-22T10:00:00Z")
        ));
        adapter.save(Loan.create(
                secondUser,
                new BigDecimal("9999.00"),
                36,
                Instant.parse("2026-07-23T10:00:00Z")
        ));

        List<Loan> result = adapter.findByUserId(firstUser);

        assertThat(result)
                .extracting(Loan::amount)
                .containsExactly(new BigDecimal("2000.00"), new BigDecimal("1000.00"));
        assertThat(result).allMatch(loan -> loan.userId().equals(firstUser));
    }

    private Long saveUser(String email) {
        return userRepository.save(new UserJpaEntity(
                null,
                email,
                "hashed-password",
                Role.USER,
                true,
                Instant.parse("2026-07-01T00:00:00Z")
        )).getId();
    }
}
