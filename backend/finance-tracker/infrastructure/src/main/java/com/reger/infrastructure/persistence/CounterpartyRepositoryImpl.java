package com.reger.infrastructure.persistence;

import com.reger.domain.entity.Counterparty;
import com.reger.domain.entity.CounterpartyType;
import com.reger.domain.repository.CounterpartyRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CounterpartyRepositoryImpl implements CounterpartyRepository {

    private final CounterpartyJpaRepository counterpartyJpaRepository;
    private final EntityManager entityManager;

    @Override
    public List<Counterparty> findAll(String name) {
        if (name == null || name.isBlank()) {
            return counterpartyJpaRepository.findAll();
        }
        return counterpartyJpaRepository.findByNameContainsIgnoreCase(name);
    }

    @Override
    public Optional<Counterparty> findById(UUID id) {
        return counterpartyJpaRepository.findById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return counterpartyJpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByName(String name, UUID excludeId) {
        return counterpartyJpaRepository.existsByNameIgnoreCaseAndIdNot(name, excludeId);
    }

    @Override
    public Counterparty create(Counterparty counterparty) {
        return counterpartyJpaRepository.save(counterparty);
    }

    @Override
    public Counterparty update(UUID id, String name, CounterpartyType type, String description) {
        var counterparty = entityManager.getReference(Counterparty.class, id);
        counterparty.setName(name);
        counterparty.setType(type);
        counterparty.setDescription(description);
        return counterpartyJpaRepository.save(counterparty);
    }

    @Override
    public void delete(UUID id) {
        counterpartyJpaRepository.deleteById(id);
    }
}
