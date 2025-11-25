package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.api.TransactionsApi;
import org.example.converters.TransactionRequestToTransactionConverter;
import org.example.model.TransactionRequest;
import org.example.models.Transaction;
import org.example.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionsApi {
    private final TransactionService transactionService;
    private final TransactionRequestToTransactionConverter transactionRequestToTransactionConverter;

    @Override
    public ResponseEntity<Void> createTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = transactionRequestToTransactionConverter.convert(transactionRequest);
        transactionService.transferAmount(transaction);
        return ResponseEntity.ok().build();
    }
}
