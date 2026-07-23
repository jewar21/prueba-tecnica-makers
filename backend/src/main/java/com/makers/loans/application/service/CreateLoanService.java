package com.makers.loans.application.service;

import com.makers.loans.application.dto.CreateLoanCommand;
import com.makers.loans.application.port.in.CreateLoanUseCase;
import com.makers.loans.domain.exception.AuthenticatedUserNotFoundException;
import com.makers.loans.domain.model.Loan;
import com.makers.loans.domain.model.User;
import com.makers.loans.domain.port.out.LoadUserByEmailPort;
import com.makers.loans.domain.port.out.SaveLoanPort;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class CreateLoanService implements CreateLoanUseCase {

    private final LoadUserByEmailPort userLoader;
    private final SaveLoanPort loanSaver;
    private final Clock clock;

    public CreateLoanService(LoadUserByEmailPort userLoader, SaveLoanPort loanSaver, Clock clock) {
        this.userLoader = userLoader;
        this.loanSaver = loanSaver;
        this.clock = clock;
    }

    @Override
    public Loan create(CreateLoanCommand command) {
        User user = userLoader.loadByEmail(command.authenticatedEmail())
                .filter(User::enabled)
                .orElseThrow(AuthenticatedUserNotFoundException::new);
        Loan loan = Loan.create(user.id(), command.amount(), command.termMonths(), clock.instant());
        return loanSaver.save(loan);
    }
}
