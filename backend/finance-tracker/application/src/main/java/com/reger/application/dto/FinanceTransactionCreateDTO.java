package com.reger.application.dto;

import com.reger.domain.entity.AccountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record FinanceTransactionCreateDTO(
        @NotNull Boolean isPlan,
        @NotNull OffsetDateTime dateTime,
        @Nullable @Null UUID payerId,
        @Nullable @Null UUID recipientId,
        @NotNull AccountType accountType,
        @Size(max = 500) String comment,
        @NotNull @Valid List<FinanceTransactionDetailCreateDTO> details
) {
    public FinanceTransactionCreateDTO {
        if (accountType == AccountType.Credit && recipientId == null) {
            throw new IllegalArgumentException("Account type = Credit. RecipientId must be not null");
        }
        if (accountType == AccountType.Debit && payerId == null) {
            throw new IllegalArgumentException("Account type = Debit. PayerId must be not null");
        }
    }
}