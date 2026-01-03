package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.TransportDao;
import org.example.dto.DriverTransportStatsDto;
import org.example.dto.TransportDto;
import org.example.mapper.TransportMapper;
import org.example.dao.VehicleDao;
import org.example.dao.EmployeeDao;
import org.example.dao.ClientDao;
import org.example.dao.CompanyDao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for generating reports on transport company operations.
 * Requirement #9: Display reports on transports, drivers, and revenue.
 */
public class ReportService {
    private static final Logger log = LogManager.getLogger(ReportService.class);
    private final TransportDao transportDao;
    private final TransportMapper transportMapper;

    public ReportService() {
        this.transportDao = new TransportDao();
        // Initialize mapper with required DAOs
        this.transportMapper = new TransportMapper(
            new VehicleDao(), 
            new EmployeeDao(), 
            new ClientDao(), 
            new CompanyDao()
        );
    }

    /**
     * Get total number of transports performed by a company.
     *
     * @param companyId Company ID
     * @return Total count of transports
     */
    public long getTotalTransportsCount(Integer companyId) {
        log.info("Fetching total transport count for company {}", companyId);
        return transportDao.countByCompanyId(companyId);
    }

    /**
     * Get total revenue from all transports for a company within a time period.
     *
     * @param companyId Company ID
     * @param from      Start date (inclusive), null for no lower bound
     * @param to        End date (inclusive), null for no upper bound
     * @return Total revenue
     */
    public BigDecimal getTotalRevenue(Integer companyId, LocalDateTime from, LocalDateTime to) {
        log.info("Fetching total revenue for company {} between {} and {}", companyId, from, to);
        return transportDao.sumRevenueByCompanyBetween(companyId, from, to);
    }

    /**
     * Get statistics for each driver: transport count and revenue generated.
     *
     * @param companyId Company ID
     * @return List of driver statistics ordered by transport count descending
     */
    public List<DriverTransportStatsDto> getDriverStatistics(Integer companyId) {
        log.info("Fetching driver statistics for company {}", companyId);
        return transportDao.driverStatsByCompany(companyId);
    }

    /**
     * Get all transports for a specific driver.
     *
     * @param driverId Driver (employee) ID
     * @return List of transports performed by the driver
     */
    public List<TransportDto> getTransportsByDriver(Integer driverId) {
        log.info("Fetching transports for driver {}", driverId);
        return transportDao.findByDriverId(driverId).stream()
                .map(transportMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get transports within a date range.
     *
     * @param from Start date (inclusive), null for no lower bound
     * @param to   End date (inclusive), null for no upper bound
     * @return List of transports in the date range
     */
    public List<TransportDto> getTransportsByDateRange(LocalDateTime from, LocalDateTime to) {
        log.info("Fetching transports between {} and {}", from, to);
        return transportDao.findBetweenDates(from, to).stream()
                .map(transportMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Generate a complete report summary for a company.
     *
     * @param companyId Company ID
     * @param from      Start date for revenue calculation, null for all time
     * @param to        End date for revenue calculation, null for all time
     * @return Report summary string
     */
    public String generateCompanyReportSummary(Integer companyId, LocalDateTime from, LocalDateTime to) {
        long totalTransports = getTotalTransportsCount(companyId);
        BigDecimal totalRevenue = getTotalRevenue(companyId, from, to);
        List<DriverTransportStatsDto> driverStats = getDriverStatistics(companyId);

        StringBuilder report = new StringBuilder();
        report.append("=== Company Report ===\n");
        report.append(String.format("Company ID: %d\n", companyId));
        report.append(String.format("Total Transports: %d\n", totalTransports));
        report.append(String.format("Total Revenue: %.2f\n", totalRevenue));
        report.append("\nDriver Statistics:\n");
        report.append(String.format("%-20s %-15s %-15s\n", "Driver Name", "Transport Count", "Revenue"));
        report.append("=".repeat(50)).append("\n");

        for (DriverTransportStatsDto stat : driverStats) {
            report.append(String.format("%-20s %-15d %.2f\n",
                    stat.driverName(),
                    stat.transportCount(),
                    stat.totalRevenue()));
        }

        log.info("Generated report summary for company {}", companyId);
        return report.toString();
    }
}
