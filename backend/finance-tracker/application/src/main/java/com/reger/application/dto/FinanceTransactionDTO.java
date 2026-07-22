package com.reger.application.dto;

import com.reger.domain.entity.AccountType;
import com.reger.domain.entity.FinanceTransaction;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record FinanceTransactionDTO(
        UUID id,
        Boolean isPlan,
        OffsetDateTime dateTime,
        UserResponse responsibleUser,
        CounterpartyDTO payer,
        CounterpartyDTO recipient,
        AccountType accountType,
        BigDecimal price,
        String comment,
        List<FinanceTransactionDetailDTO> details
) {
    public static FinanceTransactionDTO from(FinanceTransaction transaction) {
        return new FinanceTransactionDTO(
                transaction.getId(),
                transaction.getIsPlan(),
                transaction.getDateTime(),
                UserResponse.from(transaction.getResponsibleUser()),
                CounterpartyDTO.from(transaction.getPayer()),
                CounterpartyDTO.from(transaction.getRecipient()),
                transaction.getAccountType(),
                transaction.getPrice(),
                transaction.getComment(),
                transaction.getDetails().stream()
                        .map(FinanceTransactionDetailDTO::from)
                        .toList()
        );
    }
}
