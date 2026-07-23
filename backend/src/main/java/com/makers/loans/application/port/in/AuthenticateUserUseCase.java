package com.makers.loans.application.port.in;

import com.makers.loans.application.dto.AuthenticationResult;
import com.makers.loans.application.dto.LoginCommand;

public interface AuthenticateUserUseCase {
    AuthenticationResult authenticate(LoginCommand command);
}
