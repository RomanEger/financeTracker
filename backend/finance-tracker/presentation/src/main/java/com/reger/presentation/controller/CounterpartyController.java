package com.reger.presentation.controller;

import com.reger.application.ConflictException;
import com.reger.application.NotFoundException;
import com.reger.application.dto.CounterpartyCreateDTO;
import com.reger.application.dto.CounterpartyDTO;
import com.reger.application.dto.CounterpartyUpdateDTO;
import com.reger.application.service.CounterpartyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/counterparty")
@RequiredArgsConstructor
@Tag(name = "Counterparty")
public class CounterpartyController {

    private final CounterpartyService counterpartyService;

    @GetMapping
    public ResponseEntity<List<CounterpartyDTO>> getCounterparties(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(counterpartyService.getAll(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CounterpartyDTO> getCounterpartyById(@PathVariable UUID id) throws NotFoundException {
        return ResponseEntity.ok(counterpartyService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CounterpartyDTO> createCounterparty(@Valid @RequestBody CounterpartyCreateDTO counterpartyDTO) throws ConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(counterpartyService.create(counterpartyDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<CounterpartyDTO> updateCounterparty(@Valid @RequestBody CounterpartyUpdateDTO counterpartyDTO) throws NotFoundException, ConflictException {
        return ResponseEntity.ok(counterpartyService.update(counterpartyDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCounterparty(@PathVariable UUID id) {
        counterpartyService.deleteById(id);
    }
}