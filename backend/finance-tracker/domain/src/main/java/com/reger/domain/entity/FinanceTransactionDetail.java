package com.reger.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "finance_transaction_details")
public class FinanceTransactionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "finance_transaction_detail_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private FinanceTransaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "product_name", length = 300, nullable = false)
    private String productName;

    @Column(name = "price", precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    public static FinanceTransactionDetail create(Category category, String productName, BigDecimal price) {
        FinanceTransactionDetail detail = new FinanceTransactionDetail();
        detail.setCategory(category);
        detail.setProductName(productName);
        detail.setPrice(price);
        return detail;
    }
}