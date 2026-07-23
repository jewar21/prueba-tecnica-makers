package com.makers.loans.infrastructure.persistence.repository;

import com.makers.loans.infrastructure.persistence.entity.LoanJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanJpaRepository extends JpaRepository<LoanJpaEntity, Long> {
    List<LoanJpaEntity> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}
