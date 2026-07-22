package com.reger.domain.filter;

import java.util.UUID;

public record UserFinanceTransactionFilter(UUID userId, UUID financeTransactionId) {
}