package org.example.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CompanyDto(
        Integer id,
        String name,
        String address,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
     public static CompanyDto create(String name, String address, String phone) {
        return new CompanyDto(null, name, address, phone, null, null);
    }
}
