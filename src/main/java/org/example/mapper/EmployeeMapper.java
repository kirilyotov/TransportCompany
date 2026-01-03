package org.example.mapper;

import org.example.dto.EmployeeDto;
import org.example.entity.Employee;

public final class EmployeeMapper implements EntityMapper<Employee, EmployeeDto> {
    private static final EmployeeMapper INSTANCE = new EmployeeMapper();
    
    
    private EmployeeMapper() { }
    
    public static EmployeeMapper getInstance() {
        return INSTANCE;
    }
    
    @Override
    public Employee toEntity(EmployeeDto dto) {
        Employee employee = Employee.builder()
                .name(dto.name())
                .ucn(dto.ucn())
                .phone(dto.phone())
                .salary(dto.salary())
                .qualifications(dto.qualifications())
                .build();
        
        if (dto.id() != null) {
            employee.setId(dto.id());
        }
    
        
        return employee;
    }
    
    @Override
    public EmployeeDto toDto(Employee entity) {
        return new EmployeeDto(
                entity.getId(),
                entity.getName(),
                entity.getUcn(),
                entity.getPhone(),
                entity.getSalary(),
                entity.getQualifications(),
                entity.getCompany() != null ? entity.getCompany().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
