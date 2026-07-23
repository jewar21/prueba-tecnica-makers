package com.makers.loans.domain.port.out;

import com.makers.loans.domain.model.User;

public interface TokenIssuerPort {
    String issue(User user);
}
