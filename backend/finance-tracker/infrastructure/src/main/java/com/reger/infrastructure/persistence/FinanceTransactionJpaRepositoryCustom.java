package com.reger.infrastructure.persistence;

import com.reger.domain.entity.FinanceTransaction;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface FinanceTransactionJpaRepositoryCustom {
    public Optional<FinanceTransaction> findOneForUpdate(Specification<FinanceTransaction> spec);
}