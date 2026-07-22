package com.reger.domain.repository;

import com.reger.domain.entity.Counterparty;
import com.reger.domain.entity.CounterpartyType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CounterpartyRepository {
    List<Counterparty> findAll(String name);

    Optional<Counterparty> findById(UUID id);

    Counterparty getDefault();

    boolean existsByName(String name);

    boolean existsByName(String name, UUID excludeId);

    Counterparty create(Counterparty counterparty);

    Counterparty update(UUID id, String name, CounterpartyType type, String description);

    void delete(UUID id);
}