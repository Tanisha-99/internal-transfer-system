package org.example.service;

import org.example.models.Account;

public interface AccountService {
    void createAccount(Account account);

    Account getAccountByAccountNumber(Long accountId);
}
