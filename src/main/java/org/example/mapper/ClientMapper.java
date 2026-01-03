package org.example.mapper;

import org.example.dao.CompanyDao;
import org.example.dto.ClientDto;
import org.example.entity.Client;
import org.example.entity.Company;

public final class ClientMapper implements EntityMapper<Client, ClientDto> {
    
    private final CompanyDao companyDao;

    public ClientMapper(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }
    
    @Override
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
        
        if (dto.companyId() != null) {
            Company company = companyDao.findById(dto.companyId());
            client.setCompany(company);
        }
        
        return client;
    }
    
    @Override
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
