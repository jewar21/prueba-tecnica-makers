package com.makers.loans.infrastructure.web.controller;

import com.makers.loans.application.dto.LoginCommand;
import com.makers.loans.application.port.in.AuthenticateUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUser;

    public AuthController(AuthenticateUserUseCase authenticateUser) {
        this.authenticateUser = authenticateUser;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var result = authenticateUser.authenticate(new LoginCommand(request.email(), request.password()));
        return ResponseEntity.ok(LoginResponse.from(result));
    }
}
