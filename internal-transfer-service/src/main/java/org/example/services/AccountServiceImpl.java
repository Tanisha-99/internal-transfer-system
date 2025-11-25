package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.AccountAlreadyExistsException;
import org.example.AccountNotFoundException;
import org.example.InsufficientBalanceException;
import org.example.converters.AccountEntityToAccountConverter;
import org.example.converters.AccountToAccountEntityConverter;
import org.example.entities.AccountEntity;
import org.example.models.Account;
import org.example.repositories.AccountRepository;
import org.example.service.AccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountToAccountEntityConverter accountToAccountEntityConverter;
    private final AccountEntityToAccountConverter accountEntityToAccountConverter;

    @Override
    public void createAccount(Account account) {
        accountRepository.findByAccountId(account.getAccountId())
                .ifPresent(a -> {
                    throw new AccountAlreadyExistsException(
                            "Account already exists: " + account.getAccountId()
                    );
                });

        if(account.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientBalanceException("Account balance should be greater than 0 to create an account");
        }

        AccountEntity accountEntity = accountToAccountEntityConverter.convert(account);

        accountRepository.save(accountEntity);
    }

    @Override
    public Account getAccountByAccountNumber(Long accountId) {
        AccountEntity accountEntity = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        return accountEntityToAccountConverter.convert(accountEntity);
    }
}
