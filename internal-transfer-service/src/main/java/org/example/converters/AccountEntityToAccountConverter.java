package org.example.converters;

import org.example.entities.AccountEntity;
import org.example.models.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountEntityToAccountConverter {
    public Account convert(AccountEntity accountEntity) {
        return Account.builder()
                .accountId(accountEntity.getAccountId())
                .balance(accountEntity.getBalance())
                .build();
    }
}
