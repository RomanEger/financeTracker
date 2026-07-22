package com.reger.domain.repository;

import com.reger.domain.entity.FinanceTransaction;
import com.reger.domain.filter.FinanceTransactionFilter;
import com.reger.domain.filter.UserFinanceTransactionFilter;

import java.util.List;
import java.util.Optional;

public interface FinanceTransactionRepository {
    List<FinanceTransaction> findAll(FinanceTransactionFilter filter);

    Optional<FinanceTransaction> find(UserFinanceTransactionFilter filter);

    Optional<FinanceTransaction> findForUpdate(UserFinanceTransactionFilter filter);

    FinanceTransaction create(FinanceTransaction transaction);

    FinanceTransaction update(FinanceTransaction transaction);

    void delete(UserFinanceTransactionFilter filter);
}