package org.example.services;

import org.example.AccountNotFoundException;
import org.example.InsufficientBalanceException;
import org.example.entities.AccountEntity;
import org.example.models.Transaction;
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
@DisplayName("TransactionServiceImpl Tests")
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private AccountEntity sourceAccount;
    private AccountEntity destinationAccount;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        sourceAccount = new AccountEntity()
                .setId(1L)
                .setAccountId(1001L)
                .setBalance(new BigDecimal("1000.00"));

        destinationAccount = new AccountEntity()
                .setId(2L)
                .setAccountId(1002L)
                .setBalance(new BigDecimal("500.00"));

        transaction = Transaction.builder()
                .sourceAccountId(1001L)
                .destinationAccountId(1002L)
                .amount(new BigDecimal("100.00"))
                .build();
    }

    @Test
    @DisplayName("Should transfer amount successfully")
    void shouldTransferAmountSuccessfully() {
        // Given
        when(accountRepository.findByAccountIdForUpdate(1001L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountIdForUpdate(1002L)).thenReturn(Optional.of(destinationAccount));
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        transactionService.transferAmount(transaction);

        // Then
        assertThat(sourceAccount.getBalance()).isEqualByComparingTo(new BigDecimal("900.00"));
        assertThat(destinationAccount.getBalance()).isEqualByComparingTo(new BigDecimal("600.00"));

        verify(accountRepository).findByAccountIdForUpdate(1001L);
        verify(accountRepository).findByAccountIdForUpdate(1002L);
        verify(accountRepository, times(2)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException when amount is zero")
    void shouldThrowExceptionWhenAmountIsZero() {
        // Given
        Transaction zeroAmountTransaction = Transaction.builder()
                .sourceAccountId(1001L)
                .destinationAccountId(1002L)
                .amount(BigDecimal.ZERO)
                .build();

        // When & Then
        assertThatThrownBy(() -> transactionService.transferAmount(zeroAmountTransaction))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Transfer amount must be greater than zero");

        verify(accountRepository, never()).findByAccountIdForUpdate(any());
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException when amount is negative")
    void shouldThrowExceptionWhenAmountIsNegative() {
        // Given
        Transaction negativeAmountTransaction = Transaction.builder()
                .sourceAccountId(1001L)
                .destinationAccountId(1002L)
                .amount(new BigDecimal("-50.00"))
                .build();

        // When & Then
        assertThatThrownBy(() -> transactionService.transferAmount(negativeAmountTransaction))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Transfer amount must be greater than zero");

        verify(accountRepository, never()).findByAccountIdForUpdate(any());
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when source account not found")
    void shouldThrowExceptionWhenSourceAccountNotFound() {
        // Given
        when(accountRepository.findByAccountIdForUpdate(1001L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transactionService.transferAmount(transaction))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Source account not found: 1001");

        verify(accountRepository).findByAccountIdForUpdate(1001L);
        verify(accountRepository, never()).findByAccountIdForUpdate(1002L);
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when destination account not found")
    void shouldThrowExceptionWhenDestinationAccountNotFound() {
        // Given
        when(accountRepository.findByAccountIdForUpdate(1001L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountIdForUpdate(1002L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transactionService.transferAmount(transaction))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("Destination account not found: 1002");

        verify(accountRepository).findByAccountIdForUpdate(1001L);
        verify(accountRepository).findByAccountIdForUpdate(1002L);
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InsufficientBalanceException when source account has insufficient balance")
    void shouldThrowExceptionWhenInsufficientBalance() {
        // Given
        Transaction largeAmountTransaction = Transaction.builder()
                .sourceAccountId(1001L)
                .destinationAccountId(1002L)
                .amount(new BigDecimal("2000.00"))
                .build();

        when(accountRepository.findByAccountIdForUpdate(1001L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountIdForUpdate(1002L)).thenReturn(Optional.of(destinationAccount));

        // When & Then
        assertThatThrownBy(() -> transactionService.transferAmount(largeAmountTransaction))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance in source account: 1001");

        verify(accountRepository).findByAccountIdForUpdate(1001L);
        verify(accountRepository).findByAccountIdForUpdate(1002L);
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should transfer exact balance from source account")
    void shouldTransferExactBalanceFromSourceAccount() {
        // Given
        Transaction exactBalanceTransaction = Transaction.builder()
                .sourceAccountId(1001L)
                .destinationAccountId(1002L)
                .amount(new BigDecimal("1000.00"))
                .build();

        when(accountRepository.findByAccountIdForUpdate(1001L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountIdForUpdate(1002L)).thenReturn(Optional.of(destinationAccount));
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        transactionService.transferAmount(exactBalanceTransaction);

        // Then
        assertThat(sourceAccount.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(destinationAccount.getBalance()).isEqualByComparingTo(new BigDecimal("1500.00"));

        verify(accountRepository, times(2)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("Should transfer minimum amount successfully")
    void shouldTransferMinimumAmountSuccessfully() {
        // Given
        Transaction minAmountTransaction = Transaction.builder()
                .sourceAccountId(1001L)
                .destinationAccountId(1002L)
                .amount(new BigDecimal("0.01"))
                .build();

        when(accountRepository.findByAccountIdForUpdate(1001L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountIdForUpdate(1002L)).thenReturn(Optional.of(destinationAccount));
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        transactionService.transferAmount(minAmountTransaction);

        // Then
        assertThat(sourceAccount.getBalance()).isEqualByComparingTo(new BigDecimal("999.99"));
        assertThat(destinationAccount.getBalance()).isEqualByComparingTo(new BigDecimal("500.01"));

        verify(accountRepository, times(2)).save(any(AccountEntity.class));
    }

    @Test
    @DisplayName("Should fail when trying to overdraw by small amount")
    void shouldFailWhenTryingToOverdrawBySmallAmount() {
        // Given
        Transaction overdrawTransaction = Transaction.builder()
                .sourceAccountId(1001L)
                .destinationAccountId(1002L)
                .amount(new BigDecimal("1000.01"))
                .build();

        when(accountRepository.findByAccountIdForUpdate(1001L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountIdForUpdate(1002L)).thenReturn(Optional.of(destinationAccount));

        // When & Then
        assertThatThrownBy(() -> transactionService.transferAmount(overdrawTransaction))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("Insufficient balance in source account: 1001");

        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle self-transfer (same account as source and destination)")
    void shouldHandleSelfTransfer() {
        // Given
        Transaction selfTransferTransaction = Transaction.builder()
                .sourceAccountId(1001L)
                .destinationAccountId(1001L)
                .amount(new BigDecimal("100.00"))
                .build();

        when(accountRepository.findByAccountIdForUpdate(1001L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        transactionService.transferAmount(selfTransferTransaction);

        // Then
        // Balance should remain same after self-transfer (subtract and add same amount)
        assertThat(sourceAccount.getBalance()).isEqualByComparingTo(new BigDecimal("1000.00"));

        verify(accountRepository, times(2)).findByAccountIdForUpdate(1001L);
        verify(accountRepository, times(2)).save(any(AccountEntity.class));
    }
}
