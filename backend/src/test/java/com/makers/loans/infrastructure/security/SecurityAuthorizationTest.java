package com.makers.loans.infrastructure.security;

import com.makers.loans.domain.model.Role;
import com.makers.loans.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityAuthorizationTest.ProtectedTestControllerConfig.class)
class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenAdapter tokenAdapter;

    @Test
    void rejectsAnonymousRequestWithUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void rejectsUserRoleWithForbidden() throws Exception {
        mockMvc.perform(get("/api/admin/test")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(Role.USER)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Acceso denegado"));
    }

    @Test
    void allowsAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/test")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken(Role.ADMIN)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("available"));
    }

    @Test
    void allowsAngularPreflightForLogin() throws Exception {
        mockMvc.perform(options("/api/auth/login")
                        .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header()
                        .string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200"));
    }

    private String bearerToken(Role role) {
        User user = new User(1L, role.name().toLowerCase() + "@makers.com", "hash", role, true, Instant.now());
        return "Bearer " + tokenAdapter.issue(user);
    }

    @TestConfiguration
    static class ProtectedTestControllerConfig {

        @RestController
        static class ProtectedTestController {

            @GetMapping("/api/admin/test")
            Map<String, String> test() {
                return Map.of("status", "available");
            }
        }
    }
}
