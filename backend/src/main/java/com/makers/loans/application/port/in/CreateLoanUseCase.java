package com.makers.loans.application.port.in;

import com.makers.loans.application.dto.CreateLoanCommand;
import com.makers.loans.domain.model.Loan;

public interface CreateLoanUseCase {
    Loan create(CreateLoanCommand command);
}
