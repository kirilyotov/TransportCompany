package org.example.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ClientDto(
        Integer id,
        String name,
        String surname,
        String phone,
        String email,
        String address,
        Integer companyId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
