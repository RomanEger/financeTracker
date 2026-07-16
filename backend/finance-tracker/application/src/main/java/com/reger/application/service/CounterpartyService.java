package com.reger.application.service;

import com.reger.application.ConflictException;
import com.reger.application.NotFoundException;
import com.reger.application.dto.CounterpartyCreateDTO;
import com.reger.application.dto.CounterpartyDTO;
import com.reger.application.dto.CounterpartyUpdateDTO;
import com.reger.domain.entity.Counterparty;
import com.reger.domain.repository.CounterpartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CounterpartyService {

    private final CounterpartyRepository counterpartyRepository;

    public List<CounterpartyDTO> getAll(String name) {
        return counterpartyRepository.findAll(name)
                .stream()
                .map(CounterpartyDTO::from)
                .toList();
    }

    public CounterpartyDTO findById(UUID counterpartyId) throws NotFoundException {
        return counterpartyRepository.findById(counterpartyId)
                .map(CounterpartyDTO::from)
                .orElseThrow(() -> new NotFoundException("Counterparty not found. Id: " + counterpartyId));
    }

    @Transactional
    public CounterpartyDTO create(CounterpartyCreateDTO counterpartyDTO) throws ConflictException {
        if (counterpartyRepository.existsByName(counterpartyDTO.name())) {
            throw new ConflictException("Counterparty with name " + counterpartyDTO.name() + " already exists");
        }

        var counterparty = Counterparty.create(counterpartyDTO.name(), counterpartyDTO.type(), counterpartyDTO.description());
        var newCounterparty = counterpartyRepository.create(counterparty);

        return CounterpartyDTO.from(newCounterparty);
    }

    @Transactional
    public CounterpartyDTO update(CounterpartyUpdateDTO counterpartyDTO) throws NotFoundException, ConflictException {
        var counterpartyId = counterpartyDTO.id();
        counterpartyRepository.findById(counterpartyId)
                .orElseThrow(() -> new NotFoundException("Counterparty not found. Id: " + counterpartyId));

        if (counterpartyRepository.existsByName(counterpartyDTO.name(), counterpartyId)) {
            throw new ConflictException("Counterparty with name " + counterpartyDTO.name() + " already exists");
        }

        var updatedCounterparty = counterpartyRepository.update(
                counterpartyId,
                counterpartyDTO.name(),
                counterpartyDTO.type(),
                counterpartyDTO.description()
        );
        return CounterpartyDTO.from(updatedCounterparty);
    }

    @Transactional
    public void deleteById(UUID counterpartyId) {
        counterpartyRepository.delete(counterpartyId);
    }
}
