package com.makers.loans.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoanTest {

    private static final Instant NOW = Instant.parse("2026-07-23T17:00:00Z");

    @Test
    void createsNewLoanInPendingState() {
        Loan loan = Loan.create(7L, new BigDecimal("1250000.00"), 24, NOW);

        assertThat(loan.id()).isNull();
        assertThat(loan.userId()).isEqualTo(7L);
        assertThat(loan.amount()).isEqualByComparingTo("1250000.00");
        assertThat(loan.termMonths()).isEqualTo(24);
        assertThat(loan.status()).isEqualTo(LoanStatus.PENDING);
        assertThat(loan.createdAt()).isEqualTo(NOW);
        assertThat(loan.updatedAt()).isEqualTo(NOW);
    }

    @Test
    void rejectsMissingUserIdentity() {
        assertThatThrownBy(() -> Loan.create(null, new BigDecimal("1000.00"), 12, NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User id is required");
    }

    @Test
    void rejectsNullZeroAndNegativeAmounts() {
        assertThatThrownBy(() -> Loan.create(1L, null, 12, NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than zero");
        assertThatThrownBy(() -> Loan.create(1L, BigDecimal.ZERO, 12, NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than zero");
        assertThatThrownBy(() -> Loan.create(1L, new BigDecimal("-0.01"), 12, NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than zero");
    }

    @Test
    void rejectsAmountWithMoreThanTwoDecimalPlaces() {
        assertThatThrownBy(() -> Loan.create(1L, new BigDecimal("100.001"), 12, NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must have at most two decimal places");
    }

    @Test
    void rejectsTermOutsideOneToThreeHundredSixtyMonths() {
        assertThatThrownBy(() -> Loan.create(1L, new BigDecimal("1000.00"), 0, NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Term must be between 1 and 360 months");
        assertThatThrownBy(() -> Loan.create(1L, new BigDecimal("1000.00"), 361, NOW))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Term must be between 1 and 360 months");
    }

    @Test
    void rejectsMissingCreationTime() {
        assertThatThrownBy(() -> Loan.create(1L, new BigDecimal("1000.00"), 12, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Creation time is required");
    }
}
