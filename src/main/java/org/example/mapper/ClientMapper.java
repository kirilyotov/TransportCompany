package org.example.mapper;

import org.example.dto.ClientDto;
import org.example.entity.Client;

public final class ClientMapper {
    
    private static final ClientMapper INSTANCE = new ClientMapper();
    
    private ClientMapper() {}
    
    public static ClientMapper getInstance() {
        return INSTANCE;
    }
    
    public Client toEntity(ClientDto dto) {
        Client client = Client.builder()
                .name(dto.name())
                .surname(dto.surname())
                .phone(dto.phone())
                .email(dto.email())
                .address(dto.address())
                .build();
        
        if (dto.id() != null) {
            client.setId(dto.id());
        }
        return client;
    }
    
    public ClientDto toDto(Client entity) {
        return new ClientDto(
                entity.getId(),
                entity.getName(),
                entity.getSurname(),
                entity.getPhone(),
                entity.getEmail(),
                entity.getAddress(),
                entity.getCompany() != null ? entity.getCompany().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}