package org.example.mapper;

import org.example.dao.CompanyDao;
import org.example.dto.VehicleDto;
import org.example.entity.Vehicle;
import org.example.entity.Company;

public final class VehicleMapper implements EntityMapper<Vehicle, VehicleDto> {
    
    private static final VehicleMapper INSTANCE = new VehicleMapper();
    
    private VehicleMapper() { }
    
    public static VehicleMapper getInstance() {
        return INSTANCE;
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
