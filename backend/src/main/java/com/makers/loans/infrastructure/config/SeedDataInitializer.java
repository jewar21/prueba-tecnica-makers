package com.makers.loans.infrastructure.config;

import com.makers.loans.domain.model.Role;
import com.makers.loans.infrastructure.persistence.entity.UserJpaEntity;
import com.makers.loans.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@Component
public class SeedDataInitializer implements ApplicationRunner {

    private final UserJpaRepository users;
    private final PasswordEncoder passwordEncoder;
    private final String userEmail;
    private final String userPassword;
    private final String adminEmail;
    private final String adminPassword;

    public SeedDataInitializer(
            UserJpaRepository users,
            PasswordEncoder passwordEncoder,
            @Value("${app.seed.user-email:}") String userEmail,
            @Value("${app.seed.user-password:}") String userPassword,
            @Value("${app.seed.admin-email:}") String adminEmail,
            @Value("${app.seed.admin-password:}") String adminPassword
    ) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments arguments) {
        List<String> values = List.of(userEmail, userPassword, adminEmail, adminPassword);
        if (values.stream().allMatch(String::isBlank)) {
            return;
        }
        if (values.stream().anyMatch(String::isBlank)) {
            throw new IllegalStateException("Seed configuration requires all USER and ADMIN environment variables");
        }

        String normalizedUserEmail = normalizeEmail(userEmail);
        String normalizedAdminEmail = normalizeEmail(adminEmail);
        if (normalizedUserEmail.equals(normalizedAdminEmail)) {
            throw new IllegalStateException("Seed USER and ADMIN emails must be different");
        }

        createIfMissing(normalizedUserEmail, userPassword, Role.USER);
        createIfMissing(normalizedAdminEmail, adminPassword, Role.ADMIN);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private void createIfMissing(String email, String rawPassword, Role role) {
        if (users.existsByEmailIgnoreCase(email)) {
            return;
        }
        users.save(new UserJpaEntity(
                null,
                email,
                passwordEncoder.encode(rawPassword),
                role,
                true,
                Instant.now()
        ));
    }
}
