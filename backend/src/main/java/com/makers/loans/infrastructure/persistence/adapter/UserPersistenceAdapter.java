package com.makers.loans.infrastructure.persistence.adapter;

import com.makers.loans.domain.model.User;
import com.makers.loans.domain.port.out.LoadUserByEmailPort;
import com.makers.loans.infrastructure.persistence.entity.UserJpaEntity;
import com.makers.loans.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
public class UserPersistenceAdapter implements LoadUserByEmailPort {

    private final UserJpaRepository repository;

    public UserPersistenceAdapter(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> loadByEmail(String email) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        return repository.findByEmailIgnoreCase(normalizedEmail).map(this::toDomain);
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRole(),
                entity.isEnabled(),
                entity.getCreatedAt()
        );
    }
}
