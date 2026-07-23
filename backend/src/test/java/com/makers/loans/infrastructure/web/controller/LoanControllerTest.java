package com.makers.loans.infrastructure.web.controller;

import com.makers.loans.application.dto.CreateLoanCommand;
import com.makers.loans.application.port.in.CreateLoanUseCase;
import com.makers.loans.application.port.in.GetOwnLoansUseCase;
import com.makers.loans.domain.model.Loan;
import com.makers.loans.domain.model.Role;
import com.makers.loans.domain.model.User;
import com.makers.loans.infrastructure.security.JwtTokenAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoanControllerTest {

    private static final Instant NOW = Instant.parse("2026-07-23T19:00:00Z");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenAdapter tokenAdapter;

    @MockitoBean
    private CreateLoanUseCase createLoan;

    @MockitoBean
    private GetOwnLoansUseCase getOwnLoans;

    @Test
    void createsPendingLoanForJwtSubject() throws Exception {
        Loan saved = new Loan(12L, 1L, new BigDecimal("1250000.00"), 24,
                com.makers.loans.domain.model.LoanStatus.PENDING, NOW, NOW);
        when(createLoan.create(new CreateLoanCommand("user@makers.com", new BigDecimal("1250000.00"), 24)))
                .thenReturn(saved);

        mockMvc.perform(post("/api/loans")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("user@makers.com", Role.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":1250000.00,"termMonths":24}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "/api/loans/12"))
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.termMonths").value(24));

        verify(createLoan).create(new CreateLoanCommand("user@makers.com", new BigDecimal("1250000.00"), 24));
    }

    @Test
    void returnsClearFieldErrorsForInvalidLoanRequest() throws Exception {
        mockMvc.perform(post("/api/loans")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("user@makers.com", Role.USER))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":0,"termMonths":361}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.amount").value("El monto debe ser mayor que cero"))
                .andExpect(jsonPath("$.errors.termMonths").value("El plazo debe estar entre 1 y 360 meses"));
    }

    @Test
    void listsOnlyLoansResolvedForJwtSubject() throws Exception {
        Loan loan = new Loan(15L, 1L, new BigDecimal("5000.00"), 6,
                com.makers.loans.domain.model.LoanStatus.PENDING, NOW, NOW);
        when(getOwnLoans.getByAuthenticatedEmail("user@makers.com")).thenReturn(List.of(loan));

        mockMvc.perform(get("/api/loans/my")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("user@makers.com", Role.USER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(15))
                .andExpect(jsonPath("$[0].amount").value(5000.00));

        verify(getOwnLoans).getByAuthenticatedEmail("user@makers.com");
    }

    @Test
    void returnsEmptyArrayWhenUserHasNoLoans() throws Exception {
        when(getOwnLoans.getByAuthenticatedEmail("empty@makers.com")).thenReturn(List.of());

        mockMvc.perform(get("/api/loans/my")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("empty@makers.com", Role.USER)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void rejectsAnonymousRequests() throws Exception {
        mockMvc.perform(get("/api/loans/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsAdminRoleFromUserLoanEndpoints() throws Exception {
        mockMvc.perform(post("/api/loans")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("admin@makers.com", Role.ADMIN))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount":1000.00,"termMonths":12}
                                """))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/loans/my")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken("admin@makers.com", Role.ADMIN)))
                .andExpect(status().isForbidden());
    }

    private String bearerToken(String email, Role role) {
        User user = new User(1L, email, "hash", role, true, NOW);
        return "Bearer " + tokenAdapter.issue(user);
    }
}
