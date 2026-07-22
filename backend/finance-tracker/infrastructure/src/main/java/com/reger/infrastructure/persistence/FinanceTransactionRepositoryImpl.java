package com.reger.infrastructure.persistence;

import com.reger.application.NotFoundException;
import com.reger.domain.entity.FinanceTransaction;
import com.reger.domain.filter.FinanceTransactionFilter;
import com.reger.domain.filter.UserFinanceTransactionFilter;
import com.reger.domain.repository.FinanceTransactionRepository;
import com.reger.domain.repository.UserRepository;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.DeleteSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FinanceTransactionRepositoryImpl implements FinanceTransactionRepository {

    private final FinanceTransactionJpaRepository financeTransactionJpaRepository;
    private final UserRepository userRepository;

    @Override
    public List<FinanceTransaction> findAll(FinanceTransactionFilter filter) {
        var spec = buildSpecification(filter);
        return financeTransactionJpaRepository.findAll(spec);
    }

    @Override
    public Optional<FinanceTransaction> find(UserFinanceTransactionFilter filter) {
        var spec = buildSpecification(filter);
        return financeTransactionJpaRepository.findOne(spec);
    }

    @Override
    public Optional<FinanceTransaction> findForUpdate(UserFinanceTransactionFilter filter) {
        var spec = buildPredicateSpecification(filter);
        return financeTransactionJpaRepository.findOneForUpdate(spec);
    }

    @Override
    public FinanceTransaction create(FinanceTransaction transaction) {
        return financeTransactionJpaRepository.save(transaction);
    }

    @Override
    public FinanceTransaction update(FinanceTransaction transaction) {
        return financeTransactionJpaRepository.save(transaction);
    }

    @Override
    public void delete(UserFinanceTransactionFilter filter) {
        var spec = buildDeleteSpecification(filter);
        financeTransactionJpaRepository.delete(spec);
    }

    private Specification<FinanceTransaction> buildSpecification(FinanceTransactionFilter filter)
            throws IllegalArgumentException {
        return (root, _, cb) -> {
            rootFetch(root);
            List<Predicate> predicates;
            try {
                predicates = getPredicatesWithUser(cb, root, filter.userId());
            } catch (NotFoundException e) {
                return null;
            }

            if (filter.isPlan() != null) {
                predicates.add(cb.equal(root.get("isPlan"), filter.isPlan()));
            }
            if (filter.dateTimeFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateTime"), filter.dateTimeFrom()));
            }
            if (filter.dateTimeTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dateTime"), filter.dateTimeTo()));
            }
            if (filter.counterpartyId() != null) {
                Join<Object, Object> payer = root.join("payer");
                Join<Object, Object> recipient = root.join("recipient");
                predicates.add(cb.or(
                        cb.equal(payer.get("id"), filter.counterpartyId()),
                        cb.equal(recipient.get("id"), filter.counterpartyId())
                ));
            }

            return cb.and(predicates);
        };
    }

    private Specification<FinanceTransaction> buildSpecification(UserFinanceTransactionFilter filter)
            throws IllegalArgumentException {
        return (root, _, cb) -> {
            rootFetch(root);
            try {
                return getPredicate(filter, root, cb);
            } catch (NotFoundException e) {
                return null;
            }
        };
    }

    private Specification<FinanceTransaction> buildPredicateSpecification(UserFinanceTransactionFilter filter) {
        return (root, _, cb) -> {
            try {
                return getPredicate(filter, root, cb);
            } catch (NotFoundException e) {
                return null;
            }
        };
    }

    private DeleteSpecification<FinanceTransaction> buildDeleteSpecification(UserFinanceTransactionFilter filter) {
        return (root, _, cb) -> {
            try {
                return getPredicate(filter, root, cb);
            } catch (NotFoundException e) {
                return null;
            }
        };
    }

    private Predicate getPredicate(UserFinanceTransactionFilter filter, Root<FinanceTransaction> root, CriteriaBuilder cb)
            throws IllegalArgumentException, NotFoundException {
        var predicates = getPredicatesWithUser(cb, root, filter.userId());
        predicates.add(cb.equal(root.get("id"), filter.financeTransactionId()));

        return cb.and(predicates);
    }

    private void rootFetch(Root<FinanceTransaction> root) {
        root.fetch("details", JoinType.INNER).fetch("category", JoinType.INNER);
        root.fetch("responsibleUser", JoinType.INNER);
        root.fetch("payer", JoinType.INNER);
        root.fetch("recipient", JoinType.INNER);
    }

    private List<Predicate> getPredicatesWithUser(CriteriaBuilder cb, Root<FinanceTransaction> root, UUID userId)
            throws NotFoundException, IllegalArgumentException {
        var predicates = new ArrayList<Predicate>();

        if (userId == null) {
            throw new IllegalArgumentException("UserId must be not null");
        }
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found. ID: " + userId));
        predicates.add(cb.equal(root.get("responsibleUser"), user));

        return predicates;
    }
}