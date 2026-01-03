package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.ClientDao;
import org.example.dao.EmployeeDao;
import org.example.dao.VehicleDao;
import org.example.dto.ClientDto;
import org.example.dto.CompanyDto;
import org.example.dto.EmployeeDto;
import org.example.dto.VehicleDto;
import org.example.entity.Company;
import org.example.entity.Client;
import org.example.entity.Employee;
import org.example.entity.Vehicle;
import org.example.mapper.CompanyMapper;
import org.example.mapper.ClientMapper;
import org.example.mapper.EmployeeMapper;
import org.example.mapper.VehicleMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CompanyManagementService - Unified aggregate root service for all company-related operations.
 * Consolidates all CRUD operations for Company, Clients, Employees, and Vehicles.
 * Single service layer approach - all domain operations through this service.
 */
public class CompanyManagementService {
    
    // DAOs
    private final CompanyDao companyDao;
    private final ClientDao clientDao;
    private final EmployeeDao employeeDao;
    private final VehicleDao vehicleDao;
    
    // Mappers
    private final CompanyMapper companyMapper;
    private final ClientMapper clientMapper;
    private final EmployeeMapper employeeMapper;
    private final VehicleMapper vehicleMapper;

    public CompanyManagementService() {
        this.companyDao = new CompanyDao();
        this.clientDao = new ClientDao();
        this.employeeDao = new EmployeeDao();
        this.vehicleDao = new VehicleDao();
        this.companyMapper = CompanyMapper.getInstance();
        this.clientMapper =  ClientMapper.getInstance();
        this.employeeMapper = EmployeeMapper.getInstance();
        this.vehicleMapper = VehicleMapper.getInstance();
    }
    
    public CompanyManagementService(CompanyDao companyDao, ClientDao clientDao, 
                                   EmployeeDao employeeDao, VehicleDao vehicleDao) {
        this.companyDao = companyDao;
        this.clientDao = clientDao;
        this.employeeDao = employeeDao;
        this.vehicleDao = vehicleDao;
        this.companyMapper = CompanyMapper.getInstance();
        this.clientMapper = ClientMapper.getInstance();
        this.employeeMapper = EmployeeMapper.getInstance();
        this.vehicleMapper = VehicleMapper.getInstance();
    }

    // ==================== COMPANY OPERATIONS ====================
    
    /**
     * Creates a new company.
     */
    public CompanyDto createCompany(CompanyDto dto) {
        if (dto.id() != null) {
            throw new IllegalArgumentException("Cannot create company with specified ID.");
        }
        Company entity = companyMapper.toEntity(dto);
        Company saved = companyDao.save(entity);
        return companyMapper.toDto(saved);
    }
    
    /**
     * Creates a new company with name, address, and phone.
     */
    public CompanyDto createCompany(String name, String address, String phone) {
        CompanyDto dto = CompanyDto.create(name, address, phone);
        return createCompany(dto);
    }
    
    /**
     * Updates an existing company.
     */
    public CompanyDto updateCompany(int id, CompanyDto dto) {
        validateCompanyExists(id);

        Company existing = companyDao.findById(id);
        
        Company.CompanyBuilder builder = existing.toBuilder();
        
        Optional.ofNullable(dto.name()).ifPresent(builder::name);
        Optional.ofNullable(dto.address()).ifPresent(builder::address);
        Optional.ofNullable(dto.phone()).ifPresent(builder::phone);
        
        Company updated = companyDao.update(builder.build());
        return companyMapper.toDto(updated);
    }
    
    /**
     * Gets a company by ID.
     */
    public CompanyDto getCompany(int id) {
        Company entity = companyDao.findById(id);
        return companyMapper.toDto(entity);
    }
    
    /**
     * Gets all companies.
     */
    public List<CompanyDto> getAllCompanies() {
        return companyDao.findAll().stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Searches companies by name.
     */
    public List<CompanyDto> searchCompaniesByName(String name) {
        return companyDao.findByNameContaining(name).stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }
    
    
    /**
     * Deletes a company (with all its related data).
     */
    public void deleteCompany(int id) {
        // Delete all clients first
        companyDao.findById(id); // Verify company exists
        List<Client> clients = clientDao.findByCompanyId(id);
        clients.forEach(c -> clientDao.deleteById(c.getId()));
        
        // Delete all employees
        List<Employee> employees = employeeDao.findByCompanyId(id);
        employees.forEach(e -> employeeDao.deleteById(e.getId()));
        
        // Delete all vehicles
        List<Vehicle> vehicles = vehicleDao.findByCompanyId(id);
        vehicles.forEach(v -> vehicleDao.deleteById(v.getId()));
        
        // Finally delete the company
        companyDao.deleteById(id);
    }

    // ==================== CLIENT OPERATIONS ====================
    
    /**
     * Adds a new client to a company.
     */
    public ClientDto addClient(int companyId, ClientDto clientDto) {
        validateCompanyExists(companyId);
        
        // Create client with company assignment
        ClientDto clientWithCompany =  ClientDto.builder()
            .id(companyId)
            .name(clientDto.name())
            .surname(clientDto.surname())
            .phone(clientDto.phone())
            .email(clientDto.email())
            .address(clientDto.address())
            .companyId(companyId)
            .build();
        
        Client entity = clientMapper.toEntity(clientWithCompany);
        Client saved = clientDao.save(entity);
        return clientMapper.toDto(saved);
    }
    
    /**
     * Gets all clients for a company.
     */
    public List<ClientDto> getCompanyClients(int companyId) {
        validateCompanyExists(companyId);
        return clientDao.findByCompanyId(companyId).stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Updates a client.
     */
    public ClientDto updateClient(int clientId, ClientDto dto) {
        validateClientExists(clientId);

        Client existing = clientDao.findById(clientId);
        Client.ClientBuilder builder = existing.toBuilder();
        Optional.ofNullable(dto.name()).ifPresent(builder::name);
        Optional.ofNullable(dto.surname()).ifPresent(builder::surname);
        Optional.ofNullable(dto.phone()).ifPresent(builder::phone);
        Optional.ofNullable(dto.email()).ifPresent(builder::email);
        Optional.ofNullable(dto.address()).ifPresent(builder::address);
        Client updated = clientDao.update(builder.build());
        return clientMapper.toDto(updated);
    }
    
    /**
     * Gets a client by ID.
     */
    public ClientDto getClient(int clientId) {
        if (!clientDao.clientExists(clientId)) {
            throw new IllegalArgumentException("Client with ID " + clientId + " does not exist.");
        }
        Client entity = clientDao.findById(clientId);
        return clientMapper.toDto(entity);
    }
    
    /**
     * Removes a client from a company.
     */
    public void removeClient(int clientId) {
        validateClientExists(clientId);
        clientDao.deleteById(clientId);
    }
    
    /**
     * Transfers a client to another company.
     */
    public ClientDto transferClient(int clientId, int newCompanyId) {
        validateCompanyExists(newCompanyId);
        validateClientExists(clientId);
        
        // Get existing client
        Client client = clientDao.findById(clientId);
        
        // Update with new company
        client.setCompany(companyDao.findById(newCompanyId));
        Client updated = clientDao.update(client);
        return clientMapper.toDto(updated);
    }

    // ==================== EMPLOYEE OPERATIONS ====================
    
    /**
     * Adds a new employee to a company.
     */
    public EmployeeDto addEmployee(int companyId, EmployeeDto employeeDto) {
       validateCompanyExists(companyId);
        
        // Create employee with company assignment
        EmployeeDto employeeWithCompany = EmployeeDto.builder()
            .name(employeeDto.name())
            .ucn(employeeDto.ucn())
            .qualifications(employeeDto.qualifications())
            .salary(employeeDto.salary())
            .companyId(companyId)
            .build();
        
        Employee entity = employeeMapper.toEntity(employeeWithCompany);
        Employee saved = employeeDao.save(entity);
        return employeeMapper.toDto(saved);
    }
    
    /**
     * Gets all employees for a company.
     */
    public List<EmployeeDto> getCompanyEmployees(int companyId) {
        validateCompanyExists(companyId);
        validateEmployeeExists(companyId);

        return employeeDao.findByCompanyId(companyId).stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Updates an employee.
     */
    public EmployeeDto updateEmployee(int employeeId, EmployeeDto dto) {
        validateEmployeeExists(employeeId);
        Employee existing = employeeDao.findById(employeeId);
        Employee.EmployeeBuilder builder = existing.toBuilder();
        Optional.ofNullable(dto.name()).ifPresent(builder::name);
        Optional.ofNullable(dto.ucn()).ifPresent(builder::ucn);
        Optional.ofNullable(dto.phone()).ifPresent(builder::phone);
        Optional.ofNullable(dto.salary()).ifPresent(builder::salary);
        Optional.ofNullable(dto.qualifications()).ifPresent(builder::qualifications);
        Employee updated = employeeDao.update(builder.build());
        return employeeMapper.toDto(updated);
    }
    
    /**
     * Gets an employee by ID.
     */
    public EmployeeDto getEmployee(int employeeId) {
        Employee entity = employeeDao.findById(employeeId);
        return employeeMapper.toDto(entity);
    }
    
    /**
     * Removes an employee from a company.
     */
    public void removeEmployee(int employeeId) {
        validateEmployeeExists(employeeId);

        employeeDao.deleteById(employeeId);
    }
    
    /**
     * Transfers an employee to another company.
     */
    public EmployeeDto transferEmployee(int employeeId, int newCompanyId) {
        validateEmployeeExists(employeeId);
        validateCompanyExists(newCompanyId);
                
        // Get existing employee
        Employee employee = employeeDao.findById(employeeId);
        
        // Update with new company
        employee.setCompany(companyDao.findById(newCompanyId));
        Employee updated = employeeDao.update(employee);
        return employeeMapper.toDto(updated);
    }

    // ==================== VEHICLE OPERATIONS ====================
    
    /**
     * Adds a new vehicle to a company.
     */
    public VehicleDto addVehicle(int companyId, VehicleDto vehicleDto) {
        // Verify company exists
        validateCompanyExists(companyId);
        
        // Create vehicle with company assignment
        VehicleDto vehicleWithCompany = VehicleDto.builder()
        .licensePlate(vehicleDto.licensePlate())
        .type(vehicleDto.type())
        .capacityWeight(vehicleDto.capacityWeight())
        .capacityPassengers(vehicleDto.capacityPassengers())
        .companyId(companyId)
        .status(vehicleDto.status())
        .build();

        
        Vehicle entity = vehicleMapper.toEntity(vehicleWithCompany);
        Vehicle saved = vehicleDao.save(entity);
        return vehicleMapper.toDto(saved);
    }
    
    /**
     * Gets all vehicles for a company.
     */
    public List<VehicleDto> getCompanyVehicles(int companyId) {
        validateCompanyExists(companyId);

        return vehicleDao.findByCompanyId(companyId).stream()
                .map(vehicleMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Updates a vehicle.
     */
    public VehicleDto updateVehicle(int vehicleId, VehicleDto dto) {
        validateVehicleExists(vehicleId);

        Vehicle entity = vehicleMapper.toEntity(dto);
        entity.setId(vehicleId);
        Vehicle updated = vehicleDao.update(entity);
        return vehicleMapper.toDto(updated);
    }
    
    /**
     * Gets a vehicle by ID.
     */
    public VehicleDto getVehicle(int vehicleId) {
        Vehicle entity = vehicleDao.findById(vehicleId);
        return vehicleMapper.toDto(entity);
    }
    
    /**
     * Removes a vehicle from a company.
     */
    public void removeVehicle(int vehicleId) {
        validateVehicleExists(vehicleId);

        vehicleDao.deleteById(vehicleId);
    }
    
    /**
     * Transfers a vehicle to another company.
     */
    public VehicleDto transferVehicle(int vehicleId, int newCompanyId) {
        validateVehicleExists(vehicleId);
        validateCompanyExists(newCompanyId);
        
        // Get existing vehicle
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        
        // Update with new company
        vehicle.setCompany(companyDao.findById(newCompanyId));
        Vehicle updated = vehicleDao.update(vehicle);
        return vehicleMapper.toDto(updated);
    }

    private void validateCompanyExists(int companyId) {
        if (!companyDao.companyExists(companyId)) {
            throw new IllegalArgumentException("Company with ID " + companyId + " does not exist.");
        }
    }

    private void validateClientExists(int clientId) {
        if (!clientDao.clientExists(clientId)) {
            throw new IllegalArgumentException("Client with ID " + clientId + " does not exist.");
        }
    }

    private void validateEmployeeExists(int employeeId) {
        if (!employeeDao.employeeExists(employeeId)) {
            throw new IllegalArgumentException("Employee with ID " + employeeId + " does not exist.");
        }
    }
    private void validateVehicleExists(int vehicleId) {
        if (!vehicleDao.vehicleExists(vehicleId)) {
            throw new IllegalArgumentException("Vehicle with ID " + vehicleId + " does not exist.");
        }
    }
}
