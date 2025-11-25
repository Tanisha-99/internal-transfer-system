package org.example.converters;

import org.example.model.TransactionRequest;
import org.example.models.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionRequestToTransactionConverter {

    public Transaction convert(TransactionRequest transactionRequest) {
        return Transaction.builder()
                .amount(transactionRequest.getAmount())
                .sourceAccountId(transactionRequest.getSourceAccountId())
                .destinationAccountId(transactionRequest.getDestinationAccountId())
                .build();
    }
}
