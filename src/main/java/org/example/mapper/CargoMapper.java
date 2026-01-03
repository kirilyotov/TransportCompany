package org.example.mapper;

import org.example.dao.TransportDao;
import org.example.dto.CargoDto;
import org.example.entity.Cargo;
import org.example.entity.Transport;

public final class CargoMapper implements EntityMapper<Cargo, CargoDto> {
    
    private final TransportDao transportDao;
    
    public CargoMapper(TransportDao transportDao) {
        this.transportDao = transportDao;
    }
    
    @Override
    public Cargo toEntity(CargoDto dto) {
        Cargo cargo = Cargo.builder()
                .type(dto.type())
                .description(dto.description())
                .totalWeight(dto.totalWeight())
                .passengerCount(dto.passengerCount())
                .build();
        
        if (dto.id() != null) {
            cargo.setId(dto.id());
        }
        
        if (dto.transportId() != null) {
            Transport transport = transportDao.findById(dto.transportId());
            cargo.setTransport(transport);
        }
        
        return cargo;
    }
    
    @Override
    public CargoDto toDto(Cargo entity) {
        return new CargoDto(
                entity.getId(),
                entity.getTransport() != null ? entity.getTransport().getId() : null,
                entity.getType(),
                entity.getDescription(),
                entity.getTotalWeight(),
                entity.getPassengerCount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
