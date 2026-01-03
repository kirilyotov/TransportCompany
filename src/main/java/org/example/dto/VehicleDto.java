package org.example.dto;

import org.example.entity.enums.VehicleStatus;
import org.example.entity.enums.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record VehicleDto(
        Integer id,
        String licensePlate,
        VehicleType type,
        BigDecimal capacityWeight,
        Integer capacityPassengers,
        Integer companyId,
        VehicleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
