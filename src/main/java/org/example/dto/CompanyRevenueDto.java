package org.example.dto;

import java.math.BigDecimal;

public record CompanyRevenueDto(Integer companyId, String companyName, BigDecimal revenue) {
}
