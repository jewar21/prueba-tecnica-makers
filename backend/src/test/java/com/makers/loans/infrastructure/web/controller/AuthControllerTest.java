package com.makers.loans.infrastructure.web.controller;

import com.makers.loans.application.dto.AuthenticationResult;
import com.makers.loans.application.dto.LoginCommand;
import com.makers.loans.domain.model.Role;
import com.makers.loans.domain.exception.InvalidCredentialsException;
import com.makers.loans.application.port.in.AuthenticateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticateUserUseCase authenticateUser;

    @Test
    void returnsJwtAndRoleForValidCredentials() throws Exception {
        when(authenticateUser.authenticate(new LoginCommand("user@makers.com", "correct-password")))
                .thenReturn(new AuthenticationResult("signed.jwt.token", Role.USER));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"user@makers.com","password":"correct-password"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("signed.jwt.token"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void returnsGenericUnauthorizedResponseForInvalidCredentials() throws Exception {
        when(authenticateUser.authenticate(new LoginCommand("user@makers.com", "wrong-password")))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"user@makers.com","password":"wrong-password"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Credenciales inválidas"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void returnsFieldErrorsForInvalidLoginRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"not-an-email","password":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.email").value("El correo debe tener un formato válido"))
                .andExpect(jsonPath("$.errors.password").value("La contraseña es obligatoria"));
    }
}
