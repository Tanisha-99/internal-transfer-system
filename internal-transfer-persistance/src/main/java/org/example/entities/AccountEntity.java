package org.example.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Data
@Accessors(chain = true)
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long accountId;

    @Column(nullable = false, precision = 19, scale = 5)
    private BigDecimal balance;
}
