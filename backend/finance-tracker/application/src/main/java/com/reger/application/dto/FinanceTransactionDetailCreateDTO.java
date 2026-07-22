package com.reger.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record FinanceTransactionDetailCreateDTO(
        @NotNull UUID categoryId,
        @NotBlank @Size(max = 300) String productName,
        @NotNull @DecimalMin("0") BigDecimal price
) {
}
