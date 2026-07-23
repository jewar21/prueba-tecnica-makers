package com.makers.loans.application.dto;

import com.makers.loans.domain.model.Role;

public record AuthenticationResult(String token, Role role) {
}
