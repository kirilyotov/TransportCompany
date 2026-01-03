package org.example.dto;

import java.time.LocalDateTime;

public record TransportStatusDto(
        Integer id,
        Integer transportId,
        LocalDateTime statusChange,
        String oldStatus,
        String newStatus,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
