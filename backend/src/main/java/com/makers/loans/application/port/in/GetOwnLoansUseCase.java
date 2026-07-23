package com.makers.loans.application.port.in;

import com.makers.loans.domain.model.Loan;

import java.util.List;

public interface GetOwnLoansUseCase {
    List<Loan> getByAuthenticatedEmail(String authenticatedEmail);
}
