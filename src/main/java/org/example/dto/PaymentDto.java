package org.example.dto;

import org.example.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        Integer id,
        Integer transportId,
        Integer clientId,
        BigDecimal amount,
        LocalDateTime paymentDateTime,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
