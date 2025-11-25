package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.AccountNotFoundException;
import org.example.InsufficientBalanceException;
import org.example.entities.AccountEntity;
import org.example.models.Transaction;
import org.example.repositories.AccountRepository;
import org.example.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void transferAmount(Transaction transaction) {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientBalanceException("Transfer amount must be greater than zero");
        }

        AccountEntity sourceAccount = accountRepository
                .findByAccountIdForUpdate(transaction.getSourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found: " + transaction.getSourceAccountId()));

        AccountEntity destinationAccount = accountRepository
                .findByAccountIdForUpdate(transaction.getDestinationAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found: " + transaction.getDestinationAccountId()));

        if (sourceAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in source account: " + transaction.getSourceAccountId());
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transaction.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transaction.getAmount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }

}
