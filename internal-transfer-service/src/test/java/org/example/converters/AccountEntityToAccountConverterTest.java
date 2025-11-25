package org.example.converters;

import org.example.entities.AccountEntity;
import org.example.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountEntityToAccountConverter Tests")
class AccountEntityToAccountConverterTest {

    private AccountEntityToAccountConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AccountEntityToAccountConverter();
    }

    @Test
    @DisplayName("Should convert AccountEntity to Account successfully")
    void shouldConvertAccountEntityToAccount() {
        // Given
        AccountEntity accountEntity = new AccountEntity()
                .setId(1L)
                .setAccountId(12345L)
                .setBalance(new BigDecimal("2500.50"));

        // When
        Account result = converter.convert(accountEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(12345L);
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("2500.50"));
    }

    @Test
    @DisplayName("Should handle zero balance")
    void shouldHandleZeroBalance() {
        // Given
        AccountEntity accountEntity = new AccountEntity()
                .setId(2L)
                .setAccountId(67890L)
                .setBalance(BigDecimal.ZERO);

        // When
        Account result = converter.convert(accountEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(67890L);
        assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should handle precision in balance")
    void shouldHandlePrecisionInBalance() {
        // Given
        AccountEntity accountEntity = new AccountEntity()
                .setId(3L)
                .setAccountId(11111L)
                .setBalance(new BigDecimal("123.45678"));

        // When
        Account result = converter.convert(accountEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("123.45678"));
    }
}
