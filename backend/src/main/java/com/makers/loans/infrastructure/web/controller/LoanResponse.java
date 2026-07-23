package com.makers.loans.infrastructure.web.controller;

import com.makers.loans.domain.model.Loan;
import com.makers.loans.domain.model.LoanStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record LoanResponse(
        Long id,
        BigDecimal amount,
        int termMonths,
        LoanStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.id(),
                loan.amount(),
                loan.termMonths(),
                loan.status(),
                loan.createdAt(),
                loan.updatedAt()
        );
    }
}
