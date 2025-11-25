package org.example.converters;

import org.example.entities.AccountEntity;
import org.example.models.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountToAccountEntityConverter {
    public AccountEntity convert(Account account) {
        return new AccountEntity().setAccountId(account.getAccountId()).setBalance(account.getBalance());
    }
}
