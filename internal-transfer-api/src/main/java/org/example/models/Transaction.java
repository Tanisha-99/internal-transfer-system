package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private Long id;

    private Long sourceAccountId;

    private Long destinationAccountId;

    private BigDecimal amount;

    private TransactionStatus status;

    private LocalDateTime timestamp;
}
