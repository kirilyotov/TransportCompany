package org.example.mapper;

import org.example.dto.CompanyDto;
import org.example.entity.Company;


public final class CompanyMapper implements EntityMapper<Company, CompanyDto> {
    private static final CompanyMapper INSTANCE = new CompanyMapper();

    private CompanyMapper() {}
    
    public static CompanyMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Company toEntity(CompanyDto dto) {
        Company company = Company.builder()
                .name(dto.name())
                .address(dto.address())
                .phone(dto.phone())
                .build();
        
        // Only set ID if it's not null (for updates)
        if (dto.id() != null) {
            company.setId(dto.id());
        }
        
        return company;
    }

    @Override
    public CompanyDto toDto(Company entity) {
        return new CompanyDto(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getPhone(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
