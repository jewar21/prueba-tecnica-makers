package com.makers.loans.application.service;

import com.makers.loans.application.dto.CreateLoanCommand;
import com.makers.loans.domain.exception.AuthenticatedUserNotFoundException;
import com.makers.loans.domain.model.Loan;
import com.makers.loans.domain.model.LoanStatus;
import com.makers.loans.domain.model.Role;
import com.makers.loans.domain.model.User;
import com.makers.loans.domain.port.out.LoadUserByEmailPort;
import com.makers.loans.domain.port.out.SaveLoanPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateLoanServiceTest {

    private static final Instant NOW = Instant.parse("2026-07-23T17:30:00Z");
    private static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

    private final LoadUserByEmailPort userLoader = mock(LoadUserByEmailPort.class);
    private final SaveLoanPort loanSaver = mock(SaveLoanPort.class);
    private final CreateLoanService service = new CreateLoanService(userLoader, loanSaver, CLOCK);

    @Test
    void createsPendingLoanForAuthenticatedUser() {
        User user = new User(9L, "user@makers.com", "hash", Role.USER, true, NOW.minusSeconds(60));
        when(userLoader.loadByEmail("user@makers.com")).thenReturn(Optional.of(user));
        when(loanSaver.save(org.mockito.ArgumentMatchers.any(Loan.class))).thenAnswer(invocation -> {
            Loan unsaved = invocation.getArgument(0);
            return new Loan(
                    31L,
                    unsaved.userId(),
                    unsaved.amount(),
                    unsaved.termMonths(),
                    unsaved.status(),
                    unsaved.createdAt(),
                    unsaved.updatedAt()
            );
        });

        Loan result = service.create(new CreateLoanCommand(
                "user@makers.com",
                new BigDecimal("2500000.00"),
                36
        ));

        ArgumentCaptor<Loan> captor = ArgumentCaptor.forClass(Loan.class);
        verify(loanSaver).save(captor.capture());
        Loan persisted = captor.getValue();
        assertThat(persisted.userId()).isEqualTo(9L);
        assertThat(persisted.status()).isEqualTo(LoanStatus.PENDING);
        assertThat(persisted.createdAt()).isEqualTo(NOW);
        assertThat(result.id()).isEqualTo(31L);
    }

    @Test
    void rejectsMissingAuthenticatedUserWithoutPersisting() {
        when(userLoader.loadByEmail("missing@makers.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(new CreateLoanCommand(
                "missing@makers.com",
                new BigDecimal("1000.00"),
                12
        )))
                .isInstanceOf(AuthenticatedUserNotFoundException.class)
                .hasMessage("Authenticated user is unavailable");
        verify(loanSaver, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rejectsDisabledAuthenticatedUserWithoutPersisting() {
        User disabled = new User(10L, "disabled@makers.com", "hash", Role.USER, false, NOW.minusSeconds(60));
        when(userLoader.loadByEmail("disabled@makers.com")).thenReturn(Optional.of(disabled));

        assertThatThrownBy(() -> service.create(new CreateLoanCommand(
                "disabled@makers.com",
                new BigDecimal("1000.00"),
                12
        )))
                .isInstanceOf(AuthenticatedUserNotFoundException.class)
                .hasMessage("Authenticated user is unavailable");
        verify(loanSaver, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
