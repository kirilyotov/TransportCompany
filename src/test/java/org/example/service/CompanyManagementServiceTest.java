package org.example.service;

import org.example.dao.*;
import org.example.dto.ClientDto;
import org.example.dto.CompanyDto;
import org.example.dto.EmployeeDto;
import org.example.dto.VehicleDto;
import org.example.entity.Client;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Vehicle;
import org.example.entity.enums.VehicleType;
import org.example.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for CompanyManagementService.
 * Tests all CRUD operations for Company, Client, Employee, and Vehicle entities.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CompanyManagementService Tests")
class CompanyManagementServiceTest {

    @Mock
    private CompanyDao companyDao;

    @Mock
    private ClientDao clientDao;

    @Mock
    private EmployeeDao employeeDao;

    @Mock
    private VehicleDao vehicleDao;

    private CompanyManagementService service;
    @Mock
    private TransportDao transportDao;

    @Mock
    private PaymentDao paymentDao;

    @Mock
    private CargoDao cargoDao;



    @BeforeEach
    void setUp() {
        service = new CompanyManagementService(companyDao, clientDao, employeeDao, vehicleDao, transportDao, paymentDao, cargoDao);
    }

    // ==================== COMPANY OPERATIONS TESTS ====================

    @Nested
    @DisplayName("Company Operations")
    class CompanyOperationsTests {

        @Test
        @DisplayName("Should create company successfully with DTO")
        void testCreateCompanyWithDto() {
            // Arrange
            CompanyDto inputDto = new CompanyDto(null, "Tech Corp", "123 Main St", "555-1234", null, null);
            Company savedEntity = Company.builder()
                    .id(1)
                    .name("Tech Corp")
                    .address("123 Main St")
                    .phone("555-1234")
                    .build();

            when(companyDao.save(any(Company.class))).thenReturn(savedEntity);

            // Act
            CompanyDto result = service.createCompany(inputDto);

            // Assert
            assertNotNull(result);
            assertEquals("Tech Corp", result.name());
            assertEquals(1, result.id());
            verify(companyDao, times(1)).save(any(Company.class));
        }

        @Test
        @DisplayName("Should fail creating company with specified ID")
        void testCreateCompanyWithSpecifiedIdFails() {
            // Arrange
            CompanyDto inputDto = new CompanyDto(1, "Tech Corp", "123 Main St", "555-1234", null, null);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> service.createCompany(inputDto),
                    "Cannot create company with specified ID.");
            verify(companyDao, never()).save(any());
        }

        @Test
        @DisplayName("Should create company successfully with parameters")
        void testCreateCompanyWithParameters() {
            // Arrange
            Company savedEntity = Company.builder()
                    .id(2)
                    .name("Finance Inc")
                    .address("456 Oak Ave")
                    .phone("555-5678")
                    .build();

            when(companyDao.save(any(Company.class))).thenReturn(savedEntity);

            // Act
            CompanyDto result = service.createCompany("Finance Inc", "456 Oak Ave", "555-5678");

            // Assert
            assertNotNull(result);
            assertEquals("Finance Inc", result.name());
            verify(companyDao, times(1)).save(any(Company.class));
        }

        @Test
        @DisplayName("Should update company successfully")
        void testUpdateCompanySuccessfully() {
            // Arrange
            Integer companyId = 1;
            Company existing = Company.builder()
                    .id(1)
                    .name("Old Name")
                    .address("123 Main St")
                    .phone("555-1234")
                    .build();
            Company updated = Company.builder()
                    .id(1)
                    .name("New Name")
                    .address("123 Main St")
                    .phone("555-1234")
                    .build();
            CompanyDto updateDto = new CompanyDto(null, "New Name", null, null, null, null);

            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(companyDao.findById(anyInt())).thenReturn(existing);
            when(companyDao.update(any(Company.class))).thenReturn(updated);

            // Act
            CompanyDto result = service.updateCompany(companyId, updateDto);

            // Assert
            assertNotNull(result);
            assertEquals("New Name", result.name());
            verify(companyDao, times(1)).update(any(Company.class));
        }

        @Test
        @DisplayName("Should fail updating non-existent company")
        void testUpdateNonExistentCompanyFails() {
            // Arrange
            Integer companyId = 999;
            CompanyDto updateDto = new CompanyDto(null, "New Name", null, null, null, null);

            when(companyDao.companyExists(anyInt())).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> service.updateCompany(companyId, updateDto));
            verify(companyDao, never()).update(any());
        }

        @Test
        @DisplayName("Should get company by ID successfully")
        void testGetCompanySuccessfully() {
            // Arrange
            Integer companyId = 1;
            Company company = Company.builder()
                    .id(1)
                    .name("Tech Corp")
                    .address("123 Main St")
                    .phone("555-1234")
                    .build();

            when(companyDao.findById(anyInt())).thenReturn(company);

            // Act
            CompanyDto result = service.getCompany(companyId);

            // Assert
            assertNotNull(result);
            assertEquals("Tech Corp", result.name());
            verify(companyDao, times(1)).findById(companyId);
        }

        @Test
        @DisplayName("Should fail getting non-existent company")
        void testGetNonExistentCompanyFails() {
            // Arrange
            Integer companyId = 999;
            when(companyDao.findById(anyInt())).thenThrow(
                    new EntityNotFoundException("Company not found with id: " + companyId));

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> service.getCompany(companyId));
        }

        @Test
        @DisplayName("Should get all companies")
        void testGetAllCompaniesSuccessfully() {
            // Arrange
            List<Company> companies = new ArrayList<>();
            companies.add(Company.builder()
                    .id(1)
                    .name("Tech Corp")
                    .address("123 Main St")
                    .phone("555-1234")
                    .build());
            companies.add(Company.builder()
                    .id(2)
                    .name("Finance Inc")
                    .address("456 Oak Ave")
                    .phone("555-5678")
                    .build());

            when(companyDao.findAll()).thenReturn(companies);

            // Act
            List<CompanyDto> result = service.getAllCompanies();

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(companyDao, times(1)).findAll();
        }

        @Test
        @DisplayName("Should search companies by name")
        void testSearchCompaniesByNameSuccessfully() {
            // Arrange
            String searchTerm = "Tech";
            List<Company> companies = new ArrayList<>();
            companies.add(Company.builder()
                    .id(1)
                    .name("Tech Corp")
                    .address("123 Main St")
                    .phone("555-1234")
                    .build());

            when(companyDao.findByNameContaining(searchTerm)).thenReturn(companies);

            // Act
            List<CompanyDto> result = service.searchCompaniesByName(searchTerm);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Tech Corp", result.get(0).name());
            verify(companyDao, times(1)).findByNameContaining(searchTerm);
        }

        @Test
        @DisplayName("Should fail deleting non-existent company")
        void testDeleteNonExistentCompanyFails() {
            // Arrange
            Integer companyId = 999;
            when(companyDao.findById(anyInt())).thenThrow(
                    new EntityNotFoundException("Company not found with id: " + companyId));

            // Act & Assert
            assertThrows(EntityNotFoundException.class, () -> service.deleteCompany(companyId));
        }
    }

    // ==================== CLIENT OPERATIONS TESTS ====================

    @Nested
    @DisplayName("Client Operations")
    class ClientOperationsTests {

        @Test
        @DisplayName("Should add client to company successfully")
        void testAddClientSuccessfully() {
            // Arrange
            Integer companyId = 1;
            ClientDto inputDto = new ClientDto(null, "John", "Doe", "555-1111", "john@email.com",
                    "123 Client St", null, null, null);
            Company company = Company.builder()
                    .id(1)
                    .name("Tech Corp")
                    .address("123 Main St")
                    .phone("555-1234")
                    .build();
            Client savedClient = new Client();
            savedClient.setId(100);
            savedClient.setName("John");
            savedClient.setCompany(company);

            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(clientDao.save(any(Client.class))).thenReturn(savedClient);

            // Act
            ClientDto result = service.addClient(companyId, inputDto);

            // Assert
            assertNotNull(result);
            assertEquals("John", result.name());
            verify(clientDao, times(1)).save(any(Client.class));
        }

        @Test
        @DisplayName("Should fail adding client to non-existent company")
        void testAddClientToNonExistentCompanyFails() {
            // Arrange
            Integer companyId = 999;
            ClientDto inputDto = new ClientDto(null, "John", "Doe", "555-1111", "john@email.com",
                    "123 Client St", null, null, null);

            when(companyDao.companyExists(anyInt())).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> service.addClient(companyId, inputDto));
            verify(clientDao, never()).save(any());
        }

        @Test
        @DisplayName("Should get all clients for company")
        void testGetCompanyClientsSuccessfully() {
            // Arrange
            Integer companyId = 1;
            List<Client> clients = new ArrayList<>();
            Client client = new Client();
            client.setId(100);
            client.setName("John");
            clients.add(client);

            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(clientDao.findByCompanyId(anyInt())).thenReturn(clients);

            // Act
            List<ClientDto> result = service.getCompanyClients(companyId);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(clientDao, times(1)).findByCompanyId(eq(companyId));
        }

        @Test
        @DisplayName("Should fail getting clients for non-existent company")
        void testGetClientsForNonExistentCompanyFails() {
            // Arrange
            Integer companyId = 999;
            when(companyDao.companyExists(anyInt())).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> service.getCompanyClients(companyId));
        }

        @Test
        @DisplayName("Should update client successfully")
        void testUpdateClientSuccessfully() {
            // Arrange
            Integer clientId = 100;
            Client existing = new Client();
            existing.setId(100);
            existing.setName("Old Name");
            Client updated = new Client();
            updated.setId(100);
            updated.setName("New Name");
            ClientDto updateDto = new ClientDto(null, "New Name", null, null, null, null, null, null, null);

            when(clientDao.clientExists(clientId)).thenReturn(true);
            when(clientDao.findById(anyInt())).thenReturn(existing);
            when(clientDao.update(any(Client.class))).thenReturn(updated);

            // Act
            ClientDto result = service.updateClient(clientId, updateDto);

            // Assert
            assertNotNull(result);
            assertEquals("New Name", result.name());
            verify(clientDao, times(1)).update(any(Client.class));
        }

        @Test
        @DisplayName("Should get client by ID successfully")
        void testGetClientSuccessfully() {
            // Arrange
            Integer clientId = 100;
            Client client = new Client();
            client.setId(100);
            client.setName("John");

            when(clientDao.clientExists(clientId)).thenReturn(true);
            when(clientDao.findById(anyInt())).thenReturn(client);

            // Act
            ClientDto result = service.getClient(clientId);

            // Assert
            assertNotNull(result);
            assertEquals("John", result.name());
        }

        @Test
        @DisplayName("Should fail getting non-existent client")
        void testGetNonExistentClientFails() {
            // Arrange
            Integer clientId = 999;
            when(clientDao.clientExists(clientId)).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> service.getClient(clientId));
        }

        @Test
        @DisplayName("Should remove client successfully")
        void testRemoveClientSuccessfully() {
            // Arrange
            Integer clientId = 100;
            when(clientDao.clientExists(anyInt())).thenReturn(true);

            // Act
            service.removeClient(clientId);

            // Assert
            verify(clientDao, times(1)).deleteById(eq(clientId));
        }

        @Test
        @DisplayName("Should transfer client to another company successfully")
        void testTransferClientSuccessfully() {
            // Arrange
            Integer clientId = 100;
            Integer newCompanyId = 2;
            Client client = new Client();
            client.setId(100);
            client.setName("John");
            Company oldCompany = Company.builder()
                    .id(1)
                    .name("Old Corp")
                    .address("123 Main St")
                    .phone("555-1234")
                    .build();
            Company newCompany = Company.builder()
                    .id(2)
                    .name("New Corp")
                    .address("456 Oak Ave")
                    .phone("555-5678")
                    .build();
            client.setCompany(oldCompany);

            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(clientDao.clientExists(clientId)).thenReturn(true);
            when(clientDao.findById(anyInt())).thenReturn(client);
            when(companyDao.findById(anyInt())).thenReturn(newCompany);
            when(clientDao.update(any(Client.class))).thenReturn(client);

            // Act
            ClientDto result = service.transferClient(clientId, newCompanyId);

            // Assert
            assertNotNull(result);
            verify(clientDao, times(1)).update(any(Client.class));
        }
    }

    // ==================== EMPLOYEE OPERATIONS TESTS ====================

    @Nested
    @DisplayName("Employee Operations")
    class EmployeeOperationsTests {

        @Test
        @DisplayName("Should add employee to company successfully")
        void testAddEmployeeSuccessfully() {
            // Arrange
            Integer companyId = 1;
            EmployeeDto inputDto = EmployeeDto.builder()
                    .name("Jane Smith")
                    .ucn("1234567890")
                    .phone("555-1111")
                    .salary(BigDecimal.valueOf(50000))
                    .qualifications("[\"hazardous\"]")
                    .build();
            Employee savedEmployee = new Employee();
            savedEmployee.setId(200);
            savedEmployee.setName("Jane Smith");

            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(employeeDao.save(any(Employee.class))).thenReturn(savedEmployee);

            // Act
            EmployeeDto result = service.addEmployee(companyId, inputDto);

            // Assert
            assertNotNull(result);
            assertEquals("Jane Smith", result.name());
            verify(employeeDao, times(1)).save(any(Employee.class));
        }

        @Test
        @DisplayName("Should fail adding employee to non-existent company")
        void testAddEmployeeToNonExistentCompanyFails() {
            // Arrange
            Integer companyId = 999;
            EmployeeDto inputDto = EmployeeDto.builder()
                    .name("Jane Smith")
                    .ucn("1234567890")
                    .phone("555-1111")
                    .salary(BigDecimal.valueOf(50000))
                    .qualifications("[\"hazardous\"]")
                    .build();

            when(companyDao.companyExists(anyInt())).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> service.addEmployee(companyId, inputDto));
            verify(employeeDao, never()).save(any());
        }

        @Test
        @DisplayName("Should get all employees for company")
        void testGetCompanyEmployeesSuccessfully() {
            // Arrange
            Integer companyId = 1;
            List<Employee> employees = new ArrayList<>();
            Employee employee = new Employee();
            employee.setId(200);
            employee.setName("Jane Smith");
            employees.add(employee);

            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(employeeDao.employeeExists(anyInt())).thenReturn(true);
            when(employeeDao.findByCompanyId(anyInt())).thenReturn(employees);

            // Act
            List<EmployeeDto> result = service.getCompanyEmployees(companyId);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(employeeDao, times(1)).findByCompanyId(eq(companyId));
        }

        @Test
        @DisplayName("Should update employee successfully")
        void testUpdateEmployeeSuccessfully() {
            // Arrange
            Integer employeeId = 200;
            Employee existing = new Employee();
            existing.setId(200);
            existing.setName("John Doe");
            Employee updated = new Employee();
            updated.setId(200);
            updated.setName("Jane Smith");
            EmployeeDto updateDto = EmployeeDto.builder()
                    .id(200)
                    .name("Jane Smith")
                    .ucn("1234567890")
                    .phone("555-1111")
                    .salary(BigDecimal.valueOf(55000))
                    .qualifications("[\"over_12_passengers\"]")
                    .build();

            when(employeeDao.employeeExists(anyInt())).thenReturn(true);
            when(employeeDao.findById(anyInt())).thenReturn(existing);
            when(employeeDao.update(any(Employee.class))).thenReturn(updated);

            // Act
            EmployeeDto result = service.updateEmployee(employeeId, updateDto);

            // Assert
            assertNotNull(result);
            verify(employeeDao, times(1)).update(any(Employee.class));
        }

        @Test
        @DisplayName("Should get employee by ID successfully")
        void testGetEmployeeSuccessfully() {
            // Arrange
            Integer employeeId = 200;
            Employee employee = new Employee();
            employee.setId(200);
            employee.setName("Jane Smith");

            when(employeeDao.findById(anyInt())).thenReturn(employee);

            // Act
            EmployeeDto result = service.getEmployee(employeeId);

            // Assert
            assertNotNull(result);
            assertEquals("Jane Smith", result.name());
        }

        @Test
        @DisplayName("Should remove employee successfully")
        void testRemoveEmployeeSuccessfully() {
            // Arrange
            Integer employeeId = 200;
            when(employeeDao.employeeExists(anyInt())).thenReturn(true);

            // Act
            service.removeEmployee(employeeId);

            // Assert
            verify(employeeDao, times(1)).deleteById(eq(employeeId));
        }

        @Test
        @DisplayName("Should transfer employee to another company successfully")
        void testTransferEmployeeSuccessfully() {
            // Arrange
            Integer employeeId = 200;
            Integer newCompanyId = 2;
            Employee employee = new Employee();
            employee.setId(200);
            employee.setName("Jane Smith");
            Company newCompany = Company.builder()
                    .id(2)
                    .name("New Corp")
                    .address("456 Oak Ave")
                    .phone("555-5678")
                    .build();

            when(employeeDao.employeeExists(anyInt())).thenReturn(true);
            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(employeeDao.findById(anyInt())).thenReturn(employee);
            when(companyDao.findById(anyInt())).thenReturn(newCompany);
            when(employeeDao.update(any(Employee.class))).thenReturn(employee);

            // Act
            EmployeeDto result = service.transferEmployee(employeeId, newCompanyId);

            // Assert
            assertNotNull(result);
            verify(employeeDao, times(1)).update(any(Employee.class));
        }
    }

    // ==================== VEHICLE OPERATIONS TESTS ====================

    @Nested
    @DisplayName("Vehicle Operations")
    class VehicleOperationsTests {

        @Test
        @DisplayName("Should add vehicle to company successfully")
        void testAddVehicleSuccessfully() {
            // Arrange
            Integer companyId = 1;
            VehicleDto inputDto = VehicleDto.builder()
                    .licensePlate("ABC123")
                    .type(VehicleType.truck)
                    .build();
            Vehicle savedVehicle = new Vehicle();
            savedVehicle.setId(300);
            savedVehicle.setLicensePlate("ABC123");

            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(vehicleDao.save(any(Vehicle.class))).thenReturn(savedVehicle);

            // Act
            VehicleDto result = service.addVehicle(companyId, inputDto);

            // Assert
            assertNotNull(result);
            assertEquals("ABC123", result.licensePlate());
            verify(vehicleDao, times(1)).save(any(Vehicle.class));
        }

        @Test
        @DisplayName("Should fail adding vehicle to non-existent company")
        void testAddVehicleToNonExistentCompanyFails() {
            // Arrange
            Integer companyId = 999;
            VehicleDto inputDto = VehicleDto.builder()
                    .licensePlate("ABC123")
                    .type(VehicleType.truck)
                    .build();

            when(companyDao.companyExists(anyInt())).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> service.addVehicle(companyId, inputDto));
            verify(vehicleDao, never()).save(any());
        }

        @Test
        @DisplayName("Should get all vehicles for company")
        void testGetCompanyVehiclesSuccessfully() {
            // Arrange
            Integer companyId = 1;
            List<Vehicle> vehicles = new ArrayList<>();
            Vehicle vehicle = new Vehicle();
            vehicle.setId(300);
            vehicle.setLicensePlate("ABC123");
            vehicles.add(vehicle);

            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(vehicleDao.findByCompanyId(anyInt())).thenReturn(vehicles);

            // Act
            List<VehicleDto> result = service.getCompanyVehicles(companyId);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(vehicleDao, times(1)).findByCompanyId(eq(companyId));
        }


        @Test
        @DisplayName("Should get vehicle by ID successfully")
        void testGetVehicleSuccessfully() {
            // Arrange
            Integer vehicleId = 300;
            Vehicle vehicle = new Vehicle();
            vehicle.setId(300);
            vehicle.setLicensePlate("ABC123");

            when(vehicleDao.findById(anyInt())).thenReturn(vehicle);

            // Act
            VehicleDto result = service.getVehicle(vehicleId);

            // Assert
            assertNotNull(result);
            assertEquals("ABC123", result.licensePlate());
        }

        @Test
        @DisplayName("Should remove vehicle successfully")
        void testRemoveVehicleSuccessfully() {
            // Arrange
            Integer vehicleId = 300;
            when(vehicleDao.vehicleExists(anyInt())).thenReturn(true);

            // Act
            service.removeVehicle(vehicleId);

            // Assert
            verify(vehicleDao, times(1)).deleteById(eq(vehicleId));
        }

        @Test
        @DisplayName("Should transfer vehicle to another company successfully")
        void testTransferVehicleSuccessfully() {
            // Arrange
            Integer vehicleId = 300;
            Integer newCompanyId = 2;
            Vehicle vehicle = new Vehicle();
            vehicle.setId(300);
            vehicle.setLicensePlate("ABC123");
            Company newCompany = Company.builder()
                    .id(2)
                    .name("New Corp")
                    .address("456 Oak Ave")
                    .phone("555-5678")
                    .build();

            when(vehicleDao.vehicleExists(anyInt())).thenReturn(true);
            when(companyDao.companyExists(anyInt())).thenReturn(true);
            when(vehicleDao.findById(anyInt())).thenReturn(vehicle);
            when(companyDao.findById(anyInt())).thenReturn(newCompany);
            when(vehicleDao.update(any(Vehicle.class))).thenReturn(vehicle);

            // Act
            VehicleDto result = service.transferVehicle(vehicleId, newCompanyId);

            // Assert
            assertNotNull(result);
            verify(vehicleDao, times(1)).update(any(Vehicle.class));
        }
    }
}
