package com.makers.loans.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Loan(
        Long id,
        Long userId,
        BigDecimal amount,
        int termMonths,
        LoanStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public Loan {
        if (userId == null) {
            throw new IllegalArgumentException("User id is required");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (amount.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("Amount must have at most two decimal places");
        }
        if (termMonths < 1 || termMonths > 360) {
            throw new IllegalArgumentException("Term must be between 1 and 360 months");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status is required");
        }
        if (createdAt == null || updatedAt == null) {
            throw new IllegalArgumentException("Creation time is required");
        }
    }

    public static Loan create(Long userId, BigDecimal amount, int termMonths, Instant now) {
        return new Loan(null, userId, amount, termMonths, LoanStatus.PENDING, now, now);
    }
}
