package com.reger.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "counterparty_type", nullable = false, columnDefinition = "counterparty_type")
    private CounterpartyType type;

    @Column(name = "description")
    private String description;

    public static Counterparty create(String name, CounterpartyType type, String description) {
        Counterparty counterparty = new Counterparty();
        counterparty.setName(name);
        counterparty.setType(type);
        counterparty.setDescription(description);
        return counterparty;
    }
}