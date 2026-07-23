package com.makers.loans.infrastructure.persistence.adapter;

import com.makers.loans.domain.model.Loan;
import com.makers.loans.domain.port.out.FindLoansByUserIdPort;
import com.makers.loans.domain.port.out.SaveLoanPort;
import com.makers.loans.infrastructure.persistence.entity.LoanJpaEntity;
import com.makers.loans.infrastructure.persistence.repository.LoanJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanPersistenceAdapter implements SaveLoanPort, FindLoansByUserIdPort {

    private final LoanJpaRepository repository;

    public LoanPersistenceAdapter(LoanJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Loan save(Loan loan) {
        LoanJpaEntity saved = repository.save(toEntity(loan));
        return toDomain(saved);
    }

    @Override
    public List<Loan> findByUserId(Long userId) {
        return repository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toDomain)
                .toList();
    }

    private LoanJpaEntity toEntity(Loan loan) {
        return new LoanJpaEntity(
                loan.id(),
                loan.userId(),
                loan.amount(),
                loan.termMonths(),
                loan.status(),
                loan.createdAt(),
                loan.updatedAt()
        );
    }

    private Loan toDomain(LoanJpaEntity entity) {
        return new Loan(
                entity.getId(),
                entity.getUserId(),
                entity.getAmount(),
                entity.getTermMonths(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
