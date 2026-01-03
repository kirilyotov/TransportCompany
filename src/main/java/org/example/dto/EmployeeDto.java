package org.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;


@Builder
public record EmployeeDto(
        Integer id,
        String name,
        String ucn,
        String phone,
        BigDecimal salary,
        String qualifications,
        Integer companyId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
