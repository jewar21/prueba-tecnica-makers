package com.makers.loans.infrastructure.web.controller;

import com.makers.loans.domain.model.Role;
import com.makers.loans.infrastructure.persistence.entity.UserJpaEntity;
import com.makers.loans.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository users;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        users.deleteAll();
        users.save(new UserJpaEntity(
                null,
                "admin@makers.com",
                passwordEncoder.encode("correct-password"),
                Role.ADMIN,
                true,
                Instant.now()
        ));
    }

    @Test
    void authenticatesPersistedUserAndReturnsVerifiableJwt() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"admin@makers.com","password":"correct-password"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = tools.jackson.databind.json.JsonMapper.builder().build()
                .readTree(response)
                .get("token")
                .asString();

        var jwt = jwtDecoder.decode(token);
        assertThat(jwt.getSubject()).isEqualTo("admin@makers.com");
        assertThat(jwt.getClaimAsStringList("roles")).containsExactly("ADMIN");
    }
}
