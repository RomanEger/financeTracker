package com.reger.presentation.controller;

import com.reger.application.NotFoundException;
import com.reger.application.dto.FinanceTransactionCreateDTO;
import com.reger.application.dto.FinanceTransactionDTO;
import com.reger.application.dto.FinanceTransactionUpdateDTO;
import com.reger.application.service.FinanceTransactionService;
import com.reger.application.service.UserService;
import com.reger.domain.filter.FinanceTransactionFilter;
import com.reger.domain.filter.UserFinanceTransactionFilter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/finance-transaction")
@RequiredArgsConstructor
@Tag(name = "FinanceTransaction")
public class FinanceTransactionController {

    private final FinanceTransactionService financeTransactionService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<FinanceTransactionDTO>> getTransactions(
            @RequestParam(required = false) Boolean isPlan,
            @RequestParam(required = false) OffsetDateTime dateTimeFrom,
            @RequestParam(required = false) OffsetDateTime dateTimeTo,
            @RequestParam(required = false) UUID counterpartyId,
            Authentication authentication
    ) {
        var user = userService.findByUsername(authentication.getName());
        var filter = new FinanceTransactionFilter(isPlan, dateTimeFrom, dateTimeTo, counterpartyId, user.id());
        return ResponseEntity.ok(financeTransactionService.getAll(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinanceTransactionDTO> getTransactionById(@PathVariable UUID id,
                                                                    Authentication authentication) throws NotFoundException {
        var user = userService.findByUsername(authentication.getName());
        var filter = new UserFinanceTransactionFilter(user.id(), id);
        return ResponseEntity.ok(financeTransactionService.find(filter));
    }

    @PostMapping
    public ResponseEntity<FinanceTransactionDTO> createTransaction(@Valid @RequestBody FinanceTransactionCreateDTO dto,
                                                                   Authentication authentication
    ) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(financeTransactionService.create(dto, authentication.getName()));
    }

    @PutMapping
    public ResponseEntity<FinanceTransactionDTO> updateTransaction(@Valid @RequestBody FinanceTransactionUpdateDTO dto,
                                                                   Authentication authentication
    ) throws NotFoundException {
        var user = userService.findByUsername(authentication.getName());
        return ResponseEntity.ok(financeTransactionService.update(dto, user.id()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable UUID id,
                                  Authentication authentication) {
        var user = userService.findByUsername(authentication.getName());
        var filter = new UserFinanceTransactionFilter(user.id(), id);
        financeTransactionService.delete(filter);
    }
}