package com.makers.loans.domain.port.out;

import com.makers.loans.domain.model.User;

import java.util.Optional;

public interface LoadUserByEmailPort {
    Optional<User> loadByEmail(String email);
}
