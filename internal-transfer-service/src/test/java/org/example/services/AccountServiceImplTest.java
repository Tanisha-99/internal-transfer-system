package org.example.services;

import org.example.AccountAlreadyExistsException;
import org.example.AccountNotFoundException;
import org.example.InsufficientBalanceException;
import org.example.converters.AccountEntityToAccountConverter;
import org.example.converters.AccountToAccountEntityConverter;
import org.example.entities.AccountEntity;
import org.example.models.Account;
import org.example.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AccountServiceImpl Tests")
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountToAccountEntityConverter accountToAccountEntityConverter;

    @Mock
    private AccountEntityToAccountConverter accountEntityToAccountConverter;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;
    private AccountEntity testAccountEntity;

    @BeforeEach
    void setUp() {
        testAccount = Account.builder()
                .accountId(12345L)
                .balance(new BigDecimal("1000.00"))
                .build();

        testAccountEntity = new AccountEntity()
                .setAccountId(12345L)
                .setBalance(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Should create account successfully")
    void shouldCreateAccountSuccessfully() {
        // Given
        when(accountRepository.findByAccountId(testAccount.getAccountId())).thenReturn(Optional.empty());
        when(accountToAccountEntityConverter.convert(testAccount)).thenReturn(testAccountEntity);
        when(accountRepository.save(testAccountEntity)).thenReturn(testAccountEntity);

        // When
        accountService.createAccount(testAccount);

        // Then
        verify(accountRepository).findByAccountId(testAccount.getAccountId());
        verify(accountToAccountEntityConverter).convert(testAccount);
        verify(accountRepository).save(testAccountEntity);
    }

    @Test
    @DisplayName("Should throw AccountAlreadyExistsException when account already exists")
    void shouldThrowExceptionWhenAccountAlreadyExists() {
        // Given
        when(accountRepository.findByAccountId(testAccount.getAccountId()))
                .thenReturn(Optional.of(testAccountEntity));

        // When & Then
        assertThatThrownBy(() -> accountService.createAccount(testAccount))
                .isInstanceOf(AccountAlreadyExistsException.class)
                .hasMessageContaining("Account already exists: 12345");

        verify(accountRepository).findByAccountId(testAccount.getAccountId());
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException when balance is zero")
    void shouldThrowExceptionWhenBalanceIsZero() {
        // Given
        Account zeroBalanceAccount = Account.builder()
                .accountId(12345L)
                .balance(BigDecimal.ZERO)
                .build();
        when(accountRepository.findByAccountId(zeroBalanceAccount.getAccountId())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.createAccount(zeroBalanceAccount))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Account balance should be greater than 0");

        verify(accountRepository).findByAccountId(zeroBalanceAccount.getAccountId());
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException when balance is negative")
    void shouldThrowExceptionWhenBalanceIsNegative() {
        // Given
        Account negativeBalanceAccount = Account.builder()
                .accountId(12345L)
                .balance(new BigDecimal("-100.00"))
                .build();
        when(accountRepository.findByAccountId(negativeBalanceAccount.getAccountId())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.createAccount(negativeBalanceAccount))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Account balance should be greater than 0");

        verify(accountRepository).findByAccountId(negativeBalanceAccount.getAccountId());
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should get account by account number successfully")
    void shouldGetAccountByAccountNumberSuccessfully() {
        // Given
        when(accountRepository.findByAccountId(12345L)).thenReturn(Optional.of(testAccountEntity));
        when(accountEntityToAccountConverter.convert(testAccountEntity)).thenReturn(testAccount);

        // When
        Account result = accountService.getAccountByAccountNumber(12345L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(12345L);
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("1000.00"));

        verify(accountRepository).findByAccountId(12345L);
        verify(accountEntityToAccountConverter).convert(testAccountEntity);
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when account not found")
    void shouldThrowExceptionWhenAccountNotFound() {
        // Given
        when(accountRepository.findByAccountId(99999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> accountService.getAccountByAccountNumber(99999L))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Account not found: 99999");

        verify(accountRepository).findByAccountId(99999L);
        verify(accountEntityToAccountConverter, never()).convert(any());
    }

    @Test
    @DisplayName("Should create account with minimum positive balance")
    void shouldCreateAccountWithMinimumPositiveBalance() {
        // Given
        Account minBalanceAccount = Account.builder()
                .accountId(12345L)
                .balance(new BigDecimal("0.01"))
                .build();
        AccountEntity minBalanceEntity = new AccountEntity()
                .setAccountId(12345L)
                .setBalance(new BigDecimal("0.01"));

        when(accountRepository.findByAccountId(12345L)).thenReturn(Optional.empty());
        when(accountToAccountEntityConverter.convert(minBalanceAccount)).thenReturn(minBalanceEntity);
        when(accountRepository.save(minBalanceEntity)).thenReturn(minBalanceEntity);

        // When
        accountService.createAccount(minBalanceAccount);

        // Then
        verify(accountRepository).save(minBalanceEntity);
    }
}
