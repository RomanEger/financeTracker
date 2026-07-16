package com.reger.application.dto;

import com.reger.domain.entity.CounterpartyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CounterpartyCreateDTO(
        @NotBlank @Size(max = 300) String name,
        CounterpartyType type,
        String description) {
}
