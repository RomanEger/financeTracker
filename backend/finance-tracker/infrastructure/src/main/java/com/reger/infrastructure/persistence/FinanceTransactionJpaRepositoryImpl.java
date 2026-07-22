package com.reger.infrastructure.persistence;

import com.reger.domain.entity.FinanceTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@RequiredArgsConstructor
public class FinanceTransactionJpaRepositoryImpl implements FinanceTransactionJpaRepositoryCustom {
    private final EntityManager em;

    @Override
    public Optional<FinanceTransaction> findOneForUpdate(Specification<FinanceTransaction> spec) {
        var cb = em.getCriteriaBuilder();
        var query = cb.createQuery(FinanceTransaction.class);
        var root = query.from(FinanceTransaction.class);

        // Fetch Joins to avoid N+1
        root.fetch("details", JoinType.LEFT)
                .fetch("category", JoinType.LEFT);
        root.fetch("responsibleUser", JoinType.LEFT);
        root.fetch("payer", JoinType.LEFT);
        root.fetch("recipient", JoinType.LEFT);

        // Apply ownership/filter predicates
        var predicate = spec.toPredicate(root, query, cb);
        if (predicate != null) {
            query.where(predicate);
        }

        query.select(root).distinct(true);

        var result = em.createQuery(query)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();

        return result.stream().findFirst();
    }
}