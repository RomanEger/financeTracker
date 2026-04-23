package com.reger.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "counterparties")
public class Counterparty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "counterparty_id")
    private UUID id;

    @Column(name = "counterparty_name", length = 300, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "counterparty_type", nullable = false, columnDefinition = "counterparty_type")
    private CounterpartyType type;

    @Column(name = "description")
    private String description;
}