package com.reger.infrastructure.persistence;

import com.reger.domain.entity.FinanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface FinanceTransactionJpaRepository extends JpaRepository<FinanceTransaction, UUID>,
        JpaSpecificationExecutor<FinanceTransaction>, FinanceTransactionJpaRepositoryCustom {
}