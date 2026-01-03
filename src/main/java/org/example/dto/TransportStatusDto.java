package org.example.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
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
