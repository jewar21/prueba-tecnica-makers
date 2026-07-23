package com.makers.loans.domain.port.out;

import com.makers.loans.domain.model.Loan;

import java.util.List;

public interface FindLoansByUserIdPort {
    List<Loan> findByUserId(Long userId);
}
