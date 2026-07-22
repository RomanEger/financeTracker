package com.reger.application.dto;

import com.reger.domain.entity.FinanceTransactionDetail;

import java.math.BigDecimal;
import java.util.UUID;

public record FinanceTransactionDetailDTO(UUID id, CategoryDTO category, String productName, BigDecimal price) {
    public static FinanceTransactionDetailDTO from(FinanceTransactionDetail detail) {
        return new FinanceTransactionDetailDTO(
                detail.getId(),
                CategoryDTO.from(detail.getCategory()),
                detail.getProductName(),
                detail.getPrice()
        );
    }
}
