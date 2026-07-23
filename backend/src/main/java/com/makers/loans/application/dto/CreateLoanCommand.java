package com.makers.loans.application.dto;

import java.math.BigDecimal;

public record CreateLoanCommand(
        String authenticatedEmail,
        BigDecimal amount,
        int termMonths
) {
}
