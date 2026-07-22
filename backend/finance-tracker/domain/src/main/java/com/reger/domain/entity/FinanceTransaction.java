package com.reger.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "finance_transactions")
public class FinanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "finance_transaction_id")
    private UUID id;

    @Column(name = "is_plan", nullable = false)
    private Boolean isPlan;

    @Column(name = "date_time", nullable = false)
    private OffsetDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_user_id", nullable = false)
    private User responsibleUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payer_id", nullable = false)
    private Counterparty payer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private Counterparty recipient;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "account_type", nullable = false, columnDefinition = "account_type")
    private AccountType accountType;

    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "comment")
    private String comment;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinanceTransactionDetail> details = new ArrayList<>();

    public static FinanceTransaction create(Boolean isPlan, OffsetDateTime dateTime, User responsibleUser,
                                            Counterparty payer, Counterparty recipient,
                                            AccountType accountType, BigDecimal price, String comment) {
        FinanceTransaction transaction = new FinanceTransaction();
        transaction.setIsPlan(isPlan);
        transaction.setDateTime(dateTime);
        transaction.setResponsibleUser(responsibleUser);
        transaction.setPayer(payer);
        transaction.setRecipient(recipient);
        transaction.setAccountType(accountType);
        transaction.setPrice(price);
        transaction.setComment(comment);
        return transaction;
    }

    public void addDetail(FinanceTransactionDetail detail) {
        details.add(detail);
        detail.setTransaction(this);
    }

    public void removeDetail(FinanceTransactionDetail detail) {
        details.remove(detail);
        detail.setTransaction(null);
    }
}