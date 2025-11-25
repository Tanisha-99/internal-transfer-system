package org.example.converters;

import jakarta.persistence.Converter;

import org.example.model.AccountCreateRequest;
import org.example.models.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountRequestToAccountConverter {
    public Account convert(AccountCreateRequest accountRequest) {
        return Account.builder()
                .accountId(accountRequest.getAccountId())
                .balance(accountRequest.getInitialBalance())
                .build();
    }
}
