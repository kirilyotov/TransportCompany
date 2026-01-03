package org.example;

import org.example.dto.*;
import org.example.entity.enums.*;
import org.example.service.CompanyManagementService;
import org.example.service.ReportService;
import org.example.service.TransportFileService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final CompanyManagementService companyService = new CompanyManagementService();
    private static final ReportService reportService = new ReportService();
    private static final TransportFileService fileService = new TransportFileService();
    private static final String FILE_PATH = "transports_demo.json";

    public static void main(String[] args) {
        CompanyDto companyA = null;
        CompanyDto companyB = null;

        try {
            System.out.println("--- Transport Company Demo Start ---");

            // 1. Create and Edit Companies
            System.out.println("\n1. Creating and updating companies...");
            companyA = companyService.createCompany(CompanyDto.builder().name("Fast Movers").address("123 Logistics Lane").phone("0888111222").build());
            companyB = companyService.createCompany(CompanyDto.builder().name("Swift Transports").address("456 Carrier Avenue").phone("0899333444").build());
            companyService.updateCompany(companyA.id(), CompanyDto.builder().name("Fast Movers Group").phone("0888555666").build());
            System.out.println("   - Companies created and one updated.");

            // 2. Add and Update Employees, Clients, and Vehicles
            System.out.println("\n2. Adding and updating employees, clients, and vehicles...");
            EmployeeDto driver1 = companyService.addEmployee(companyA.id(), EmployeeDto.builder().name("John Driver").ucn("8501011234").qualifications("[\"CDL-A\"]").salary(new BigDecimal("55000")).build());
            EmployeeDto driver2 = companyService.addEmployee(companyB.id(), EmployeeDto.builder().name("Jane Pilot").ucn("9002022345").qualifications("[\"CDL-B\", \"Hazmat\"]").salary(new BigDecimal("62000")).build());
            companyService.updateEmployee(driver2.id(), EmployeeDto.builder().salary(new BigDecimal("65000")).build());

            ClientDto client1 = companyService.addClient(companyA.id(), ClientDto.builder().name("Global").surname("Goods").email("contact@globalgoods.com").build());
            companyService.addClient(companyB.id(), ClientDto.builder().name("Local").surname("Mart").email("orders@localmart.com").build());
            companyService.updateClient(client1.id(), ClientDto.builder().address("100 Commerce Street").build());

            VehicleDto vehicle1 = companyService.addVehicle(companyA.id(), VehicleDto.builder().licensePlate("FM-001").type(VehicleType.truck).capacityWeight(new BigDecimal("25.5")).status(VehicleStatus.active).build());
            VehicleDto vehicle2 = companyService.addVehicle(companyB.id(), VehicleDto.builder().licensePlate("ST-002").type(VehicleType.truck).capacityWeight(new BigDecimal("5.0")).status(VehicleStatus.active).build());
            companyService.updateVehicle(vehicle2.id(), VehicleDto.builder().status(VehicleStatus.in_repair).build());
            System.out.println("   - Entities created and updated successfully.");

            // 3. Add and Update Transports
            System.out.println("\n3. Adding and updating transports...");
            TransportDto transport1 = companyService.createTransport(TransportDto.builder()
                    .companyId(companyA.id()).clientId(client1.id()).driverId(driver1.id()).vehicleId(vehicle1.id())
                    .startPoint("New York").endPoint("Chicago").price(new BigDecimal("2500.00"))
                    .departureDate(LocalDateTime.now().plusDays(1)).arrivalDate(LocalDateTime.now().plusDays(3))
                    .status(TransportStatus.planned).build());

            TransportDto transport2 = companyService.createTransport(TransportDto.builder()
                    .companyId(companyB.id()).clientId(client1.id()).driverId(driver2.id()).vehicleId(vehicle2.id())
                    .startPoint("Los Angeles").endPoint("San Francisco").price(new BigDecimal("1200.00"))
                    .departureDate(LocalDateTime.now().plusDays(2)).arrivalDate(LocalDateTime.now().plusDays(2))
                    .status(TransportStatus.planned).build());
            companyService.updateTransport(transport1.id(), TransportDto.builder().status(TransportStatus.in_progress).build());
            System.out.println("   - Transports created and one updated to 'in_progress'.");

            // 4. Record and Check Payments
            System.out.println("\n4. Recording payments and checking client obligations...");
            companyService.createPayment(PaymentDto.builder().transportId(transport1.id()).clientId(client1.id()).amount(transport1.price()).status(PaymentStatus.paid).build());
            System.out.println("   - Payment recorded for Client 1. Outstanding payments: " + companyService.getOutstandingPayments(client1.id()).size());
            System.out.println("   - Client 2 has unpaid transports, implying outstanding obligations.");

            // 5. Demonstrate Filtering and Sorting
            System.out.println("\n5. --- Filtering and Sorting Demo ---");
            System.out.println("\na. Companies sorted by revenue:");
            companyService.getCompaniesSortedByRevenue(null, null)
                    .forEach(c -> System.out.println("   - " + c.companyName() + ", Revenue: " + c.revenue()));

            System.out.println("\nb. Employees with salary > 60000:");
            companyService.filterEmployeesBySalary(new BigDecimal("60000"), null)
                    .forEach(e -> System.out.println("   - " + e.name() + ", Salary: " + e.salary()));

            System.out.println("\nc. Transports with destination 'Chicago':");
            companyService.searchTransportsByDestination("Chicago")
                    .forEach(t -> System.out.println("   - Transport ID: " + t.id() + ", Destination: " + t.endPoint()));

            // 6. Save Transports to File and Display
            System.out.println("\n6. Saving transports to file: " + FILE_PATH);
            List<TransportDto> allTransports = new ArrayList<>();
            allTransports.add(companyService.getTransport(transport1.id()));
            allTransports.add(companyService.getTransport(transport2.id()));
            fileService.saveToFile(allTransports, FILE_PATH);
            System.out.println("   - Loading transports from file...");
            fileService.loadFromFile(FILE_PATH).forEach(t -> System.out.println("     - Loaded: " + t.id() + " to " + t.endPoint()));

            // 7. Display a Report
            System.out.println("\n7. Generating a full report for Company A (ID: " + companyA.id() + "):");
            System.out.println(reportService.generateCompanyReportSummary(companyA.id(), null, null));

        } catch (Exception e) {
            System.err.println("\n--- An error occurred during the demo ---");
            e.printStackTrace();
        } finally {
            // 8. Cleanup: Use the robust deleteCompany method
            System.out.println("\n8. --- Cleaning up created data ---");
            try {
                if (companyA != null) {
                    companyService.deleteCompany(companyA.id());
                    System.out.println("   - Cleaned up data for " + companyA.name());
                }
                if (companyB != null) {
                    companyService.deleteCompany(companyB.id());
                    System.out.println("   - Cleaned up data for " + companyB.name());
                }
                System.out.println("   - Cleanup successful.");
            } catch (Exception e) {
                System.err.println("   - An error occurred during cleanup. Manual database cleanup may be required.");
                e.printStackTrace();
            }
            System.out.println("\n--- Transport Company Demo End ---");
        }
    }
}
