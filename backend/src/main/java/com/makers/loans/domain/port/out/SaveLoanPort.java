package com.makers.loans.domain.port.out;

import com.makers.loans.domain.model.Loan;

public interface SaveLoanPort {
    Loan save(Loan loan);
}
