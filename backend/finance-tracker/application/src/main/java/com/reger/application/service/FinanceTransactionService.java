package com.reger.application.service;

import com.reger.application.NotFoundException;
import com.reger.application.dto.FinanceTransactionCreateDTO;
import com.reger.application.dto.FinanceTransactionDTO;
import com.reger.application.dto.FinanceTransactionDetailCreateDTO;
import com.reger.application.dto.FinanceTransactionUpdateDTO;
import com.reger.domain.entity.AccountType;
import com.reger.domain.entity.Counterparty;
import com.reger.domain.entity.FinanceTransaction;
import com.reger.domain.entity.FinanceTransactionDetail;
import com.reger.domain.filter.FinanceTransactionFilter;
import com.reger.domain.filter.UserFinanceTransactionFilter;
import com.reger.domain.repository.CategoryRepository;
import com.reger.domain.repository.CounterpartyRepository;
import com.reger.domain.repository.FinanceTransactionRepository;
import com.reger.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinanceTransactionService {

    private final FinanceTransactionRepository financeTransactionRepository;
    private final UserRepository userRepository;
    private final CounterpartyRepository counterpartyRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<FinanceTransactionDTO> getAll(FinanceTransactionFilter filter) {
        return financeTransactionRepository.findAll(filter)
                .stream()
                .map(FinanceTransactionDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public FinanceTransactionDTO find(UserFinanceTransactionFilter filter) throws NotFoundException {
        return financeTransactionRepository.find(filter)
                .map(FinanceTransactionDTO::from)
                .orElseThrow(() -> new NotFoundException("FinanceTransaction not found. Id: " +
                        filter.financeTransactionId()));
    }

    @Transactional
    public FinanceTransactionDTO create(FinanceTransactionCreateDTO dto, String username) throws NotFoundException {
        var responsibleUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found. Username: " + username));
        var payer = getPayer(dto.accountType(), dto.payerId());
        var recipient = getRecipient(dto.accountType(), dto.recipientId());

        var price = calculateTotalPrice(dto.details());

        var transaction = FinanceTransaction.create(
                dto.isPlan(), dto.dateTime(), responsibleUser,
                payer, recipient, dto.accountType(),
                price, dto.comment()
        );

        getDetails(dto.details())
                .forEach(transaction::addDetail);

        var saved = financeTransactionRepository.create(transaction);
        return FinanceTransactionDTO.from(saved);
    }

    @Transactional
    public FinanceTransactionDTO update(FinanceTransactionUpdateDTO dto, UUID userId) throws NotFoundException {
        var filter = new UserFinanceTransactionFilter(userId, dto.id());
        var payer = resolveCounterparty(dto.payerId());
        var recipient = resolveCounterparty(dto.recipientId());

        var transaction = financeTransactionRepository.findForUpdate(filter)
                .orElseThrow(() -> new NotFoundException("FinanceTransaction not found. Id: " + dto.id()));

        transaction.setIsPlan(dto.isPlan());
        transaction.setDateTime(dto.dateTime());
        transaction.setPayer(payer);
        transaction.setRecipient(recipient);
        transaction.setAccountType(dto.accountType());
        transaction.setComment(dto.comment());
        transaction.setPrice(calculateTotalPrice(dto.details()));

        transaction.getDetails().clear();

        getDetails(dto.details())
                .forEach(transaction::addDetail);

        var updated = financeTransactionRepository.update(transaction);
        return FinanceTransactionDTO.from(updated);
    }

    @Transactional
    public void delete(UserFinanceTransactionFilter filter) {
        financeTransactionRepository.delete(filter);
    }

    private Counterparty getPayer(AccountType accountType, @Nullable UUID payerId) throws NotFoundException {
        return accountType == AccountType.Credit && payerId == null
                ? counterpartyRepository.getDefault()
                : resolveCounterparty(payerId);
    }

    private Counterparty getRecipient(AccountType accountType, @Nullable UUID recipientId) throws NotFoundException {
        return accountType == AccountType.Debit && recipientId == null
                ? counterpartyRepository.getDefault()
                : resolveCounterparty(recipientId);
    }

    private Counterparty resolveCounterparty(@Nullable UUID counterpartyId) throws NotFoundException {
        return counterpartyRepository.findById(counterpartyId)
                .orElseThrow(() -> new NotFoundException("Counterparty not found. Id: " + counterpartyId));
    }

    private BigDecimal calculateTotalPrice(List<FinanceTransactionDetailCreateDTO> details) {
        return details.stream()
                .map(FinanceTransactionDetailCreateDTO::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<FinanceTransactionDetail> getDetails(List<FinanceTransactionDetailCreateDTO> detailDTOs) throws NotFoundException {
        var categoryIds = detailDTOs.stream()
                .map(FinanceTransactionDetailCreateDTO::categoryId)
                .toList();

        var categoriesById = categoryRepository.findAllById(categoryIds).stream()
                .collect(Collectors.toMap(com.reger.domain.entity.Category::getId, Function.identity()));

        List<FinanceTransactionDetail> details = new ArrayList<>();
        for (var detailDTO : detailDTOs) {
            var category = categoriesById.get(detailDTO.categoryId());
            if (category == null) {
                throw new NotFoundException("Category not found. Id: " + detailDTO.categoryId());
            }
            var detail = FinanceTransactionDetail.create(category, detailDTO.productName(), detailDTO.price());
            details.add(detail);
        }
        return details;
    }
}