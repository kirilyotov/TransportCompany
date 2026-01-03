package org.example.dto;

import org.example.entity.enums.CargoType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CargoDto(
        Integer id,
        Integer transportId,
        CargoType type,
        String description,
        BigDecimal totalWeight,
        Integer passengerCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
