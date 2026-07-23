package com.makers.loans.application.service;

import com.makers.loans.application.dto.AuthenticationResult;
import com.makers.loans.application.dto.LoginCommand;
import com.makers.loans.domain.model.Role;
import com.makers.loans.domain.model.User;
import com.makers.loans.domain.port.out.LoadUserByEmailPort;
import com.makers.loans.domain.port.out.PasswordVerifierPort;
import com.makers.loans.domain.port.out.TokenIssuerPort;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticateUserServiceTest {

    private final LoadUserByEmailPort userLoader = mock(LoadUserByEmailPort.class);
    private final PasswordVerifierPort passwordVerifier = mock(PasswordVerifierPort.class);
    private final TokenIssuerPort tokenIssuer = mock(TokenIssuerPort.class);
    private final AuthenticateUserService service = new AuthenticateUserService(userLoader, passwordVerifier, tokenIssuer);

    @Test
    void returnsTokenAndRoleForActiveUserWithValidPassword() {
        User user = new User(1L, "user@makers.com", "stored-hash", Role.USER, true, Instant.now());
        when(userLoader.loadByEmail("user@makers.com")).thenReturn(Optional.of(user));
        when(passwordVerifier.matches("correct-password", Optional.of("stored-hash"))).thenReturn(true);
        when(tokenIssuer.issue(user)).thenReturn("signed.jwt.token");

        AuthenticationResult result = service.authenticate(new LoginCommand("user@makers.com", "correct-password"));

        assertThat(result.token()).isEqualTo("signed.jwt.token");
        assertThat(result.role()).isEqualTo(Role.USER);
        verify(tokenIssuer).issue(user);
    }

    @Test
    void rejectsUnknownEmailWithGenericInvalidCredentialsError() {
        when(userLoader.loadByEmail("missing@makers.com")).thenReturn(Optional.empty());
        when(passwordVerifier.matches("any-password", Optional.empty())).thenReturn(false);

        assertThatThrownBy(() -> service.authenticate(new LoginCommand("missing@makers.com", "any-password")))
                .isInstanceOf(com.makers.loans.domain.exception.InvalidCredentialsException.class)
                .hasMessage("Credenciales inválidas");
        verify(passwordVerifier).matches("any-password", Optional.empty());
    }

    @Test
    void rejectsWrongPasswordWithSameGenericError() {
        User user = new User(1L, "user@makers.com", "stored-hash", Role.USER, true, Instant.now());
        when(userLoader.loadByEmail("user@makers.com")).thenReturn(Optional.of(user));
        when(passwordVerifier.matches("wrong-password", Optional.of("stored-hash"))).thenReturn(false);

        assertThatThrownBy(() -> service.authenticate(new LoginCommand("user@makers.com", "wrong-password")))
                .isInstanceOf(com.makers.loans.domain.exception.InvalidCredentialsException.class)
                .hasMessage("Credenciales inválidas");
    }

    @Test
    void rejectsDisabledUserWithSameGenericError() {
        User user = new User(1L, "disabled@makers.com", "stored-hash", Role.USER, false, Instant.now());
        when(userLoader.loadByEmail("disabled@makers.com")).thenReturn(Optional.of(user));
        when(passwordVerifier.matches("correct-password", Optional.of("stored-hash"))).thenReturn(true);

        assertThatThrownBy(() -> service.authenticate(new LoginCommand("disabled@makers.com", "correct-password")))
                .isInstanceOf(com.makers.loans.domain.exception.InvalidCredentialsException.class)
                .hasMessage("Credenciales inválidas");
    }
}
