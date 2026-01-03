package org.example.service;

import org.example.dao.*;
import org.example.dto.*;
import org.example.entity.*;
import org.example.mapper.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final TransportDao transportDao;
    private final PaymentDao paymentDao;
    private final CargoDao cargoDao;
    
    // Mappers
    private final CompanyMapper companyMapper;
    private final ClientMapper clientMapper;
    private final EmployeeMapper employeeMapper;
    private final VehicleMapper vehicleMapper;
    private final TransportMapper transportMapper;
    private final PaymentMapper paymentMapper;
    private final CargoMapper cargoMapper;

    public CompanyManagementService() {
        this.companyDao = new CompanyDao();
        this.clientDao = new ClientDao();
        this.employeeDao = new EmployeeDao();
        this.vehicleDao = new VehicleDao();
        this.transportDao = new TransportDao();
        this.paymentDao = new PaymentDao();
        this.cargoDao = new CargoDao();
        
        this.companyMapper = CompanyMapper.getInstance();
        this.clientMapper =  new ClientMapper(this.companyDao);
        this.employeeMapper = new EmployeeMapper(this.companyDao);
        this.vehicleMapper = new VehicleMapper(this.companyDao);
        this.transportMapper = new TransportMapper(vehicleDao, employeeDao, clientDao, companyDao);
        this.paymentMapper = new PaymentMapper(transportDao, clientDao);
        this.cargoMapper = new CargoMapper(transportDao);
    }
    
    public CompanyManagementService(CompanyDao companyDao, ClientDao clientDao, 
                                   EmployeeDao employeeDao, VehicleDao vehicleDao,
                                   TransportDao transportDao, PaymentDao paymentDao, CargoDao cargoDao) {
        this.companyDao = companyDao;
        this.clientDao = clientDao;
        this.employeeDao = employeeDao;
        this.vehicleDao = vehicleDao;
        this.transportDao = transportDao;
        this.paymentDao = paymentDao;
        this.cargoDao = cargoDao;
        
        this.companyMapper = CompanyMapper.getInstance();
        this.clientMapper = new ClientMapper(this.companyDao);
        this.employeeMapper = new EmployeeMapper(this.companyDao);
        this.vehicleMapper = new VehicleMapper(this.companyDao);
        this.transportMapper = new TransportMapper(vehicleDao, employeeDao, clientDao, companyDao);
        this.paymentMapper = new PaymentMapper(transportDao, clientDao);
        this.cargoMapper = new CargoMapper(transportDao);
    }

    // ==================== COMPANY OPERATIONS ====================
    
    public CompanyDto createCompany(CompanyDto dto) {
        if (dto.id() != null) {
            throw new IllegalArgumentException("Cannot create company with specified ID.");
        }
        Company entity = companyMapper.toEntity(dto);
        Company saved = companyDao.save(entity);
        return companyMapper.toDto(saved);
    }
    
    public CompanyDto createCompany(String name, String address, String phone) {
        CompanyDto dto = CompanyDto.create(name, address, phone);
        return createCompany(dto);
    }
    
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
    
    public CompanyDto getCompany(int id) {
        Company entity = companyDao.findById(id);
        return companyMapper.toDto(entity);
    }
    
    public List<CompanyDto> getAllCompanies() {
        return companyDao.findAll().stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<CompanyDto> searchCompaniesByName(String name) {
        return companyDao.findByNameContaining(name).stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<CompanyRevenueDto> getCompaniesSortedByRevenue(LocalDateTime from, LocalDateTime to) {
        return companyDao.fetchRevenueOrdered(from, to);
    }
    
    public void deleteCompany(int id) {
        companyDao.findById(id);
        List<Transport> transports = transportDao.findByCompanyId(id);
        for (Transport transport : transports) {
            List<Payment> payments = paymentDao.findByTransportId(transport.getId());
            payments.forEach(p -> paymentDao.deleteById(p.getId()));
        }
        transports.forEach(t -> transportDao.deleteById(t.getId()));
        companyDao.deleteById(id);
    }

    // ==================== CLIENT OPERATIONS ====================
    
    public ClientDto addClient(int companyId, ClientDto clientDto) {
        validateCompanyExists(companyId);
        ClientDto clientWithCompany =  ClientDto.builder()
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
    
    public List<ClientDto> getCompanyClients(int companyId) {
        validateCompanyExists(companyId);
        return clientDao.findByCompanyId(companyId).stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }
    
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
    
    public ClientDto getClient(int clientId) {
        if (!clientDao.clientExists(clientId)) {
            throw new IllegalArgumentException("Client with ID " + clientId + " does not exist.");
        }
        Client entity = clientDao.findById(clientId);
        return clientMapper.toDto(entity);
    }
    
    public void removeClient(int clientId) {
        validateClientExists(clientId);
        clientDao.deleteById(clientId);
    }
    
    public ClientDto transferClient(int clientId, int newCompanyId) {
        validateCompanyExists(newCompanyId);
        validateClientExists(clientId);
        Client client = clientDao.findById(clientId);
        client.setCompany(companyDao.findById(newCompanyId));
        Client updated = clientDao.update(client);
        return clientMapper.toDto(updated);
    }

    // ==================== EMPLOYEE OPERATIONS ====================
    
    public EmployeeDto addEmployee(int companyId, EmployeeDto employeeDto) {
       validateCompanyExists(companyId);
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
    
    public List<EmployeeDto> getCompanyEmployees(int companyId) {
        validateCompanyExists(companyId);
        validateEmployeeExists(companyId);
        return employeeDao.findByCompanyId(companyId).stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
    
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
    
    public EmployeeDto getEmployee(int employeeId) {
        Employee entity = employeeDao.findById(employeeId);
        return employeeMapper.toDto(entity);
    }
    
    public void removeEmployee(int employeeId) {
        validateEmployeeExists(employeeId);
        employeeDao.deleteById(employeeId);
    }
    
    public EmployeeDto transferEmployee(int employeeId, int newCompanyId) {
        validateEmployeeExists(employeeId);
        validateCompanyExists(newCompanyId);
        Employee employee = employeeDao.findById(employeeId);
        employee.setCompany(companyDao.findById(newCompanyId));
        Employee updated = employeeDao.update(employee);
        return employeeMapper.toDto(updated);
    }
    
    public List<EmployeeDto> filterEmployeesByQualification(String qualification) {
        return employeeDao.findByQualificationContains(qualification).stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public List<EmployeeDto> filterEmployeesBySalary(BigDecimal min, BigDecimal max) {
        return employeeDao.findBySalaryBetween(min, max).stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    // ==================== VEHICLE OPERATIONS ====================
    
    public VehicleDto addVehicle(int companyId, VehicleDto vehicleDto) {
        validateCompanyExists(companyId);
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
    
    public List<VehicleDto> getCompanyVehicles(int companyId) {
        validateCompanyExists(companyId);
        return vehicleDao.findByCompanyId(companyId).stream()
                .map(vehicleMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public VehicleDto updateVehicle(int vehicleId, VehicleDto dto) {
        validateVehicleExists(vehicleId);
        Vehicle existing = vehicleDao.findById(vehicleId);
        Vehicle.VehicleBuilder builder = existing.toBuilder();

        Optional.ofNullable(dto.licensePlate()).ifPresent(builder::licensePlate);
        Optional.ofNullable(dto.type()).ifPresent(builder::type);
        Optional.ofNullable(dto.capacityWeight()).ifPresent(builder::capacityWeight);
        Optional.ofNullable(dto.capacityPassengers()).ifPresent(builder::capacityPassengers);
        Optional.ofNullable(dto.status()).ifPresent(builder::status);
        if (dto.companyId() != null) builder.company(companyDao.findById(dto.companyId()));

        Vehicle updated = vehicleDao.update(builder.build());
        return vehicleMapper.toDto(updated);
    }
    
    public VehicleDto getVehicle(int vehicleId) {
        Vehicle entity = vehicleDao.findById(vehicleId);
        return vehicleMapper.toDto(entity);
    }
    
    public void removeVehicle(int vehicleId) {
        validateVehicleExists(vehicleId);
        vehicleDao.deleteById(vehicleId);
    }
    
    public VehicleDto transferVehicle(int vehicleId, int newCompanyId) {
        validateVehicleExists(vehicleId);
        validateCompanyExists(newCompanyId);
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        vehicle.setCompany(companyDao.findById(newCompanyId));
        Vehicle updated = vehicleDao.update(vehicle);
        return vehicleMapper.toDto(updated);
    }

    // ==================== TRANSPORT OPERATIONS ====================
    
    public TransportDto createTransport(TransportDto dto) {
        if (dto.id() != null) {
            throw new IllegalArgumentException("Cannot create transport with specified ID.");
        }
        if (dto.companyId() != null) {
            validateCompanyExists(dto.companyId());
        }
        Transport entity = transportMapper.toEntity(dto);
        Transport saved = transportDao.save(entity);
        return transportMapper.toDto(saved);
    }
    
    public TransportDto updateTransport(int transportId, TransportDto dto) {
        Transport existing = transportDao.findById(transportId);
        Transport.TransportBuilder builder = existing.toBuilder();
        Optional.ofNullable(dto.startPoint()).ifPresent(builder::startPoint);
        Optional.ofNullable(dto.endPoint()).ifPresent(builder::endPoint);
        Optional.ofNullable(dto.departureDate()).ifPresent(builder::departureDate);
        Optional.ofNullable(dto.arrivalDate()).ifPresent(builder::arrivalDate);
        Optional.ofNullable(dto.price()).ifPresent(builder::price);
        Optional.ofNullable(dto.status()).ifPresent(builder::status);
        if (dto.vehicleId() != null) builder.vehicle(vehicleDao.findById(dto.vehicleId()));
        if (dto.driverId() != null) builder.driver(employeeDao.findById(dto.driverId()));
        if (dto.clientId() != null) builder.client(clientDao.findById(dto.clientId()));
        if (dto.companyId() != null) builder.company(companyDao.findById(dto.companyId()));
        Transport updated = transportDao.update(builder.build());
        return transportMapper.toDto(updated);
    }
    
    public TransportDto getTransport(int transportId) {
        Transport entity = transportDao.findById(transportId);
        return transportMapper.toDto(entity);
    }
    
    public List<TransportDto> getCompanyTransports(int companyId) {
        validateCompanyExists(companyId);
        return transportDao.findByCompanyId(companyId).stream()
                .map(transportMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public void deleteTransport(int transportId) {
        transportDao.deleteById(transportId);
    }
    
    public List<TransportDto> searchTransportsByDestination(String destination) {
        return transportDao.findByDestination(destination).stream()
                .map(transportMapper::toDto)
                .collect(Collectors.toList());
    }

    // ==================== PAYMENT OPERATIONS ====================
    
    public PaymentDto createPayment(PaymentDto dto) {
        if (dto.id() != null) {
            throw new IllegalArgumentException("Cannot create payment with specified ID.");
        }
        Payment entity = paymentMapper.toEntity(dto);
        Payment saved = paymentDao.save(entity);
        return paymentMapper.toDto(saved);
    }
    
    public List<PaymentDto> getOutstandingPayments(int clientId) {
        validateClientExists(clientId);
        return paymentDao.findOutstandingByClient(clientId).stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public PaymentDto updatePayment(int paymentId, PaymentDto dto) {
        paymentDao.findById(paymentId);
        Payment entity = paymentMapper.toEntity(dto);
        entity.setId(paymentId);
        Payment updated = paymentDao.update(entity);
        return paymentMapper.toDto(updated);
    }

    public void deletePayment(int paymentId) {
        paymentDao.deleteById(paymentId);
    }

    // ==================== CARGO OPERATIONS ====================
    
    public CargoDto addCargo(int transportId, CargoDto dto) {
        transportDao.findById(transportId);
        CargoDto cargoWithTransport = CargoDto.builder()
                .transportId(transportId)
                .type(dto.type())
                .description(dto.description())
                .totalWeight(dto.totalWeight())
                .passengerCount(dto.passengerCount())
                .build();
        Cargo entity = cargoMapper.toEntity(cargoWithTransport);
        Cargo saved = cargoDao.save(entity);
        return cargoMapper.toDto(saved);
    }
    
    public List<CargoDto> getTransportCargo(int transportId) {
        return cargoDao.findByTransportId(transportId).stream()
                .map(cargoMapper::toDto)
                .collect(Collectors.toList());
    }

    // ==================== VALIDATION HELPERS ====================

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
