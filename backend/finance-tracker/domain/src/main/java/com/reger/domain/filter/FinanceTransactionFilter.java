package com.reger.domain.filter;

import java.time.OffsetDateTime;
import java.util.UUID;

public record FinanceTransactionFilter(
        Boolean isPlan,
        OffsetDateTime dateTimeFrom,
        OffsetDateTime dateTimeTo,
        UUID counterpartyId,
        UUID userId
) {
}