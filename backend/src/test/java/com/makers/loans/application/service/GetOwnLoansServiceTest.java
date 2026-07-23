package com.makers.loans.application.service;

import com.makers.loans.domain.exception.AuthenticatedUserNotFoundException;
import com.makers.loans.domain.model.Loan;
import com.makers.loans.domain.model.Role;
import com.makers.loans.domain.model.User;
import com.makers.loans.domain.port.out.FindLoansByUserIdPort;
import com.makers.loans.domain.port.out.LoadUserByEmailPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetOwnLoansServiceTest {

    private static final Instant NOW = Instant.parse("2026-07-23T18:30:00Z");

    private final LoadUserByEmailPort userLoader = mock(LoadUserByEmailPort.class);
    private final FindLoansByUserIdPort loanFinder = mock(FindLoansByUserIdPort.class);
    private final GetOwnLoansService service = new GetOwnLoansService(userLoader, loanFinder);

    @Test
    void returnsOnlyLoansForAuthenticatedUsersIdentity() {
        User user = new User(4L, "owner@makers.com", "hash", Role.USER, true, NOW);
        Loan loan = Loan.create(4L, new BigDecimal("5000.00"), 6, NOW);
        when(userLoader.loadByEmail("owner@makers.com")).thenReturn(Optional.of(user));
        when(loanFinder.findByUserId(4L)).thenReturn(List.of(loan));

        List<Loan> result = service.getByAuthenticatedEmail("owner@makers.com");

        assertThat(result).containsExactly(loan);
        verify(loanFinder).findByUserId(4L);
    }

    @Test
    void returnsEmptyListWhenAuthenticatedUserHasNoLoans() {
        User user = new User(5L, "empty@makers.com", "hash", Role.USER, true, NOW);
        when(userLoader.loadByEmail("empty@makers.com")).thenReturn(Optional.of(user));
        when(loanFinder.findByUserId(5L)).thenReturn(List.of());

        assertThat(service.getByAuthenticatedEmail("empty@makers.com")).isEmpty();
    }

    @Test
    void rejectsUnavailableAuthenticatedUserWithoutQueryingLoans() {
        when(userLoader.loadByEmail("missing@makers.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByAuthenticatedEmail("missing@makers.com"))
                .isInstanceOf(AuthenticatedUserNotFoundException.class)
                .hasMessage("Authenticated user is unavailable");
        verify(loanFinder, never()).findByUserId(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rejectsDisabledAuthenticatedUserWithoutQueryingLoans() {
        User user = new User(6L, "disabled@makers.com", "hash", Role.USER, false, NOW);
        when(userLoader.loadByEmail("disabled@makers.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> service.getByAuthenticatedEmail("disabled@makers.com"))
                .isInstanceOf(AuthenticatedUserNotFoundException.class);
        verify(loanFinder, never()).findByUserId(org.mockito.ArgumentMatchers.any());
    }
}
