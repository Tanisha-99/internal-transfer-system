package org.example.converters;

import org.example.entities.AccountEntity;
import org.example.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountToAccountEntityConverter Tests")
class AccountToAccountEntityConverterTest {

    private AccountToAccountEntityConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AccountToAccountEntityConverter();
    }

    @Test
    @DisplayName("Should convert Account to AccountEntity successfully")
    void shouldConvertAccountToAccountEntity() {
        // Given
        Account account = Account.builder()
                .accountId(12345L)
                .balance(new BigDecimal("1500.75"))
                .build();

        // When
        AccountEntity result = converter.convert(account);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(12345L);
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("1500.75"));
    }

    @Test
    @DisplayName("Should handle zero balance")
    void shouldHandleZeroBalance() {
        // Given
        Account account = Account.builder()
                .accountId(99999L)
                .balance(BigDecimal.ZERO)
                .build();

        // When
        AccountEntity result = converter.convert(account);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(99999L);
        assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should handle large balance values")
    void shouldHandleLargeBalanceValues() {
        // Given
        Account account = Account.builder()
                .accountId(11111L)
                .balance(new BigDecimal("999999999.99999"))
                .build();

        // When
        AccountEntity result = converter.convert(account);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("999999999.99999"));
    }
}
