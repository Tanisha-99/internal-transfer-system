package org.example.converters;

import org.example.model.TransactionRequest;
import org.example.models.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TransactionRequestToTransactionConverter Tests")
class TransactionRequestToTransactionConverterTest {

    private TransactionRequestToTransactionConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TransactionRequestToTransactionConverter();
    }

    @Test
    @DisplayName("Should convert TransactionRequest to Transaction successfully")
    void shouldConvertTransactionRequestToTransaction() {
        // Given
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(1001L);
        request.setDestinationAccountId(1002L);
        request.setAmount(new BigDecimal("500.00"));

        // When
        Transaction result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSourceAccountId()).isEqualTo(1001L);
        assertThat(result.getDestinationAccountId()).isEqualTo(1002L);
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    @DisplayName("Should handle minimum transaction amount")
    void shouldHandleMinimumTransactionAmount() {
        // Given
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(2001L);
        request.setDestinationAccountId(2002L);
        request.setAmount(new BigDecimal("0.01"));

        // When
        Transaction result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("0.01"));
    }

    @Test
    @DisplayName("Should handle large transaction amount")
    void shouldHandleLargeTransactionAmount() {
        // Given
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(3001L);
        request.setDestinationAccountId(3002L);
        request.setAmount(new BigDecimal("999999999.99999"));

        // When
        Transaction result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("999999999.99999"));
    }

    @Test
    @DisplayName("Should preserve amount precision")
    void shouldPreserveAmountPrecision() {
        // Given
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(4001L);
        request.setDestinationAccountId(4002L);
        request.setAmount(new BigDecimal("123.45678"));

        // When
        Transaction result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("123.45678"));
    }

    @Test
    @DisplayName("Should handle same source and destination account IDs")
    void shouldHandleSameSourceAndDestinationAccountIds() {
        // Given
        TransactionRequest request = new TransactionRequest();
        request.setSourceAccountId(5001L);
        request.setDestinationAccountId(5001L);
        request.setAmount(new BigDecimal("100.00"));

        // When
        Transaction result = converter.convert(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSourceAccountId()).isEqualTo(5001L);
        assertThat(result.getDestinationAccountId()).isEqualTo(5001L);
    }
}
