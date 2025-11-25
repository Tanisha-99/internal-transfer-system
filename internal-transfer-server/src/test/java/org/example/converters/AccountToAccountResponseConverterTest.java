package org.example.converters;

import org.example.model.AccountResponse;
import org.example.models.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountToAccountResponseConverter Tests")
class AccountToAccountResponseConverterTest {

    private AccountToAccountResponseConverter converter;

    @BeforeEach
    void setUp() {
        converter = new AccountToAccountResponseConverter();
    }

    @Test
    @DisplayName("Should convert Account to AccountResponse successfully")
    void shouldConvertAccountToAccountResponse() {
        // Given
        Account account = Account.builder()
                .accountId(12345L)
                .balance(new BigDecimal("1500.75"))
                .build();

        // When
        AccountResponse result = converter.convert(account);

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
                .accountId(67890L)
                .balance(BigDecimal.ZERO)
                .build();

        // When
        AccountResponse result = converter.convert(account);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(67890L);
        assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Should preserve balance precision")
    void shouldPreserveBalancePrecision() {
        // Given
        Account account = Account.builder()
                .accountId(11111L)
                .balance(new BigDecimal("999.12345"))
                .build();

        // When
        AccountResponse result = converter.convert(account);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("999.12345"));
    }

    @Test
    @DisplayName("Should handle large balance values")
    void shouldHandleLargeBalanceValues() {
        // Given
        Account account = Account.builder()
                .accountId(99999L)
                .balance(new BigDecimal("9876543210.98765"))
                .build();

        // When
        AccountResponse result = converter.convert(account);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("9876543210.98765"));
    }
}
