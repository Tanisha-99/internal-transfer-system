package org.example.converters;

import org.example.model.AccountCreateRequest;
import org.example.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountRequestToAccountConverter Tests")
class AccountRequestToAccountConverterTest {

    private AccountRequestToAccountConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AccountRequestToAccountConverter();
    }

    @Test
    @DisplayName("Should convert AccountCreateRequest to Account successfully")
    void shouldConvertAccountCreateRequestToAccount() {
        // Given
        AccountCreateRequest request = new AccountCreateRequest();
        request.setAccountId(12345L);
        request.setInitialBalance(new BigDecimal("1000.00"));

        // When
        Account result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(12345L);
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Should handle minimum initial balance")
    void shouldHandleMinimumInitialBalance() {
        // Given
        AccountCreateRequest request = new AccountCreateRequest();
        request.setAccountId(99999L);
        request.setInitialBalance(new BigDecimal("0.01"));

        // When
        Account result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(99999L);
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("0.01"));
    }

    @Test
    @DisplayName("Should handle large initial balance")
    void shouldHandleLargeInitialBalance() {
        // Given
        AccountCreateRequest request = new AccountCreateRequest();
        request.setAccountId(55555L);
        request.setInitialBalance(new BigDecimal("9999999999.99999"));

        // When
        Account result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("9999999999.99999"));
    }

    @Test
    @DisplayName("Should preserve decimal precision")
    void shouldPreserveDecimalPrecision() {
        // Given
        AccountCreateRequest request = new AccountCreateRequest();
        request.setAccountId(77777L);
        request.setInitialBalance(new BigDecimal("123.45678"));

        // When
        Account result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("123.45678"));
    }
}
