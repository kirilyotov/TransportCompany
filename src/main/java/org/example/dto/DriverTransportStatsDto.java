package org.example.dto;

import java.math.BigDecimal;

public record DriverTransportStatsDto(Integer driverId, String driverName, long transportCount, BigDecimal totalRevenue) {
}
