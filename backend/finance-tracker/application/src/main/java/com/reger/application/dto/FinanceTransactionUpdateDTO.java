package com.reger.application.dto;

import com.reger.domain.entity.AccountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record FinanceTransactionUpdateDTO(
        @NotNull UUID id,
        @NotNull Boolean isPlan,
        @NotNull OffsetDateTime dateTime,
        @NotNull UUID payerId,
        @NotNull UUID recipientId,
        @NotNull AccountType accountType,
        @Size(max = 500) String comment,
        @NotNull @Valid List<FinanceTransactionDetailCreateDTO> details
) {
}
