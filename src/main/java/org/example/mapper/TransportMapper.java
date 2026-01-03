package org.example.mapper;

import org.example.dao.ClientDao;
import org.example.dao.CompanyDao;
import org.example.dao.EmployeeDao;
import org.example.dao.VehicleDao;
import org.example.dto.TransportDto;
import org.example.entity.*;

public final class TransportMapper implements EntityMapper<Transport, TransportDto> {
    
    private final VehicleDao vehicleDao;
    private final EmployeeDao employeeDao;
    private final ClientDao clientDao;
    private final CompanyDao companyDao;
    
    public TransportMapper(VehicleDao vehicleDao, EmployeeDao employeeDao, 
                          ClientDao clientDao, CompanyDao companyDao) {
        this.vehicleDao = vehicleDao;
        this.employeeDao = employeeDao;
        this.clientDao = clientDao;
        this.companyDao = companyDao;
    }
    
    @Override
    public Transport toEntity(TransportDto dto) {
        Transport transport = Transport.builder()
                .startPoint(dto.startPoint())
                .endPoint(dto.endPoint())
                .departureDate(dto.departureDate())
                .arrivalDate(dto.arrivalDate())
                .price(dto.price())
                .status(dto.status())
                .build();
        
        if (dto.id() != null) {
            transport.setId(dto.id());
        }
        
        if (dto.vehicleId() != null) {
            Vehicle vehicle = vehicleDao.findById(dto.vehicleId());
            transport.setVehicle(vehicle);
        }
        
        if (dto.driverId() != null) {
            Employee driver = employeeDao.findById(dto.driverId());
            transport.setDriver(driver);
        }
        
        if (dto.clientId() != null) {
            Client client = clientDao.findById(dto.clientId());
            transport.setClient(client);
        }
        
        if (dto.companyId() != null) {
            Company company = companyDao.findById(dto.companyId());
            transport.setCompany(company);
        }
        
        return transport;
    }
    
    @Override
    public TransportDto toDto(Transport entity) {
        return new TransportDto(
                entity.getId(),
                entity.getStartPoint(),
                entity.getEndPoint(),
                entity.getDepartureDate(),
                entity.getArrivalDate(),
                entity.getVehicle() != null ? entity.getVehicle().getId() : null,
                entity.getDriver() != null ? entity.getDriver().getId() : null,
                entity.getClient() != null ? entity.getClient().getId() : null,
                entity.getPrice(),
                entity.getCompany() != null ? entity.getCompany().getId() : null,
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
