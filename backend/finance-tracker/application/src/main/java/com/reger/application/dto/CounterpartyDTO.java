package com.reger.application.dto;

import com.reger.domain.entity.Counterparty;
import com.reger.domain.entity.CounterpartyType;

import java.util.UUID;

public record CounterpartyDTO(UUID id, String name, CounterpartyType type, String description) {
    public static CounterpartyDTO from(Counterparty counterparty) {
        return new CounterpartyDTO(counterparty.getId(), counterparty.getName(), counterparty.getType(), counterparty.getDescription());
    }
}
