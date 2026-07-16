package com.reger.infrastructure.persistence;

import com.reger.domain.entity.Counterparty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CounterpartyJpaRepository extends JpaRepository<Counterparty, UUID> {

    List<Counterparty> findByNameContainsIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);
}
