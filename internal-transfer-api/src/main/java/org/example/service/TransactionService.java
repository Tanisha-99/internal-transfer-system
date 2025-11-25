package org.example.service;

import org.example.models.Transaction;

public interface TransactionService {
    void transferAmount(Transaction transaction);
}
