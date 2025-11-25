package org.example.converters;

import org.example.model.AccountResponse;
import org.example.models.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountToAccountResponseConverter {
    public AccountResponse convert(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(account.getAccountId());
        accountResponse.setBalance(account.getBalance());
        return accountResponse;
    }
}
