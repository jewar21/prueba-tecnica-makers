package com.makers.loans.infrastructure.web.controller;

import com.makers.loans.application.dto.AuthenticationResult;
import com.makers.loans.domain.model.Role;

public record LoginResponse(String token, Role role) {

    public static LoginResponse from(AuthenticationResult result) {
        return new LoginResponse(result.token(), result.role());
    }
}
