package com.makers.loans.application.service;

import com.makers.loans.application.dto.AuthenticationResult;
import com.makers.loans.application.dto.LoginCommand;
import com.makers.loans.domain.exception.InvalidCredentialsException;
import com.makers.loans.domain.model.User;
import com.makers.loans.application.port.in.AuthenticateUserUseCase;
import com.makers.loans.domain.port.out.LoadUserByEmailPort;
import com.makers.loans.domain.port.out.PasswordVerifierPort;
import com.makers.loans.domain.port.out.TokenIssuerPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticateUserService implements AuthenticateUserUseCase {

    private final LoadUserByEmailPort userLoader;
    private final PasswordVerifierPort passwordVerifier;
    private final TokenIssuerPort tokenIssuer;

    public AuthenticateUserService(
            LoadUserByEmailPort userLoader,
            PasswordVerifierPort passwordVerifier,
            TokenIssuerPort tokenIssuer
    ) {
        this.userLoader = userLoader;
        this.passwordVerifier = passwordVerifier;
        this.tokenIssuer = tokenIssuer;
    }

    @Override
    public AuthenticationResult authenticate(LoginCommand command) {
        Optional<User> loadedUser = userLoader.loadByEmail(command.email());
        boolean passwordMatches = passwordVerifier.matches(
                command.password(),
                loadedUser.map(User::passwordHash)
        );
        User user = loadedUser.orElseThrow(InvalidCredentialsException::new);
        if (!user.enabled() || !passwordMatches) {
            throw new InvalidCredentialsException();
        }
        return new AuthenticationResult(tokenIssuer.issue(user), user.role());
    }
}
