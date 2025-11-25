package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.api.AccountsApi;
import org.example.converters.AccountRequestToAccountConverter;
import org.example.converters.AccountToAccountResponseConverter;
import org.example.model.AccountCreateRequest;
import org.example.model.AccountResponse;
import org.example.models.Account;
import org.example.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountsApi {
    private final AccountService accountService;
    private final AccountRequestToAccountConverter accountRequestToAccountConverter;
    private final AccountToAccountResponseConverter accountToAccountResponseConverter;

    @Override
    public ResponseEntity<Void> createAccount(AccountCreateRequest accountCreateRequest) {
        Account account = accountRequestToAccountConverter.convert(accountCreateRequest);
        accountService.createAccount(account);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AccountResponse> getAccountById(Long accountId) {
        Account account = accountService.getAccountByAccountNumber(accountId);
        AccountResponse accountResponse = accountToAccountResponseConverter.convert(account);
        return ResponseEntity.ok(accountResponse);
    }
}
