package org.example.dto;

import org.example.entity.enums.TransportStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransportDto(
        Integer id,
        String startPoint,
        String endPoint,
        LocalDateTime departureDate,
        LocalDateTime arrivalDate,
        Integer vehicleId,
        Integer driverId,
        Integer clientId,
        BigDecimal price,
        Integer companyId,
        TransportStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
