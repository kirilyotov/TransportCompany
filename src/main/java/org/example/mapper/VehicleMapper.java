package org.example.mapper;

import org.example.dao.CompanyDao;
import org.example.dto.VehicleDto;
import org.example.entity.Vehicle;
import org.example.entity.Company;

public final class VehicleMapper implements EntityMapper<Vehicle, VehicleDto> {
    
    private final CompanyDao companyDao;

    public VehicleMapper(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }
    
    @Override
    public Vehicle toEntity(VehicleDto dto) {
        Vehicle vehicle = Vehicle.builder()
                .licensePlate(dto.licensePlate())
                .type(dto.type())
                .capacityWeight(dto.capacityWeight())
                .capacityPassengers(dto.capacityPassengers())
                .status(dto.status())
                .build();
        
        if (dto.id() != null) {
            vehicle.setId(dto.id());
        }
        
        if (dto.companyId() != null) {
            Company company = companyDao.findById(dto.companyId());
            vehicle.setCompany(company);
        }
        
        return vehicle;
    }
    
    @Override
    public VehicleDto toDto(Vehicle entity) {
        return new VehicleDto(
                entity.getId(),
                entity.getLicensePlate(),
                entity.getType(),
                entity.getCapacityWeight(),
                entity.getCapacityPassengers(),
                entity.getCompany() != null ? entity.getCompany().getId() : null,
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
