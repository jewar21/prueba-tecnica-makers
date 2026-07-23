package com.makers.loans.infrastructure.web.controller;

import com.makers.loans.application.dto.CreateLoanCommand;
import com.makers.loans.application.port.in.CreateLoanUseCase;
import com.makers.loans.application.port.in.GetOwnLoansUseCase;
import com.makers.loans.domain.model.Loan;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final CreateLoanUseCase createLoan;
    private final GetOwnLoansUseCase getOwnLoans;

    public LoanController(CreateLoanUseCase createLoan, GetOwnLoansUseCase getOwnLoans) {
        this.createLoan = createLoan;
        this.getOwnLoans = getOwnLoans;
    }

    @PostMapping
    ResponseEntity<LoanResponse> create(
            @Valid @RequestBody CreateLoanRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Loan loan = createLoan.create(new CreateLoanCommand(
                jwt.getSubject(),
                request.amount(),
                request.termMonths()
        ));
        return ResponseEntity
                .created(URI.create("/api/loans/" + loan.id()))
                .body(LoanResponse.from(loan));
    }

    @GetMapping("/my")
    List<LoanResponse> getMine(@AuthenticationPrincipal Jwt jwt) {
        return getOwnLoans.getByAuthenticatedEmail(jwt.getSubject()).stream()
                .map(LoanResponse::from)
                .toList();
    }
}
