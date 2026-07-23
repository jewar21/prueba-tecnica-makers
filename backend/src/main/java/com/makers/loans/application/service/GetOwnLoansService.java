package com.makers.loans.application.service;

import com.makers.loans.application.port.in.GetOwnLoansUseCase;
import com.makers.loans.domain.exception.AuthenticatedUserNotFoundException;
import com.makers.loans.domain.model.Loan;
import com.makers.loans.domain.model.User;
import com.makers.loans.domain.port.out.FindLoansByUserIdPort;
import com.makers.loans.domain.port.out.LoadUserByEmailPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetOwnLoansService implements GetOwnLoansUseCase {

    private final LoadUserByEmailPort userLoader;
    private final FindLoansByUserIdPort loanFinder;

    public GetOwnLoansService(LoadUserByEmailPort userLoader, FindLoansByUserIdPort loanFinder) {
        this.userLoader = userLoader;
        this.loanFinder = loanFinder;
    }

    @Override
    public List<Loan> getByAuthenticatedEmail(String authenticatedEmail) {
        User user = userLoader.loadByEmail(authenticatedEmail)
                .filter(User::enabled)
                .orElseThrow(AuthenticatedUserNotFoundException::new);
        return loanFinder.findByUserId(user.id());
    }
}
