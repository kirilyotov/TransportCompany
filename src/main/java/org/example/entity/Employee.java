package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.validator.InvalidNames;
import org.example.validator.ValidPhoneNumber;
import org.example.validator.ValidUCN;
import org.example.validator.ValidAmount;

import java.math.BigDecimal;

@Entity
@Table(name = "employees",
        indexes = {
                @Index(name = "idx_employee_company", columnList = "company_id"),
                @Index(name = "idx_employee_salary", columnList = "salary")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Employee name cannot be blank!")
    @Size(max = 100, message = "Name has to be with up to 100 characters!")
    @InvalidNames(message = "Employee name is not valid!")
    private String name;

    @Column(nullable = false, unique = true, length = 10)
    @NotBlank(message = "UCN cannot be blank!")
    @ValidUCN(message = "Invalid UCN format!")
    private String ucn;

    @Column(length = 20)
    @ValidPhoneNumber(message = "Invalid phone number format!")
    private String phone;

    @Column(precision = 10, scale = 2)
    @ValidAmount(message = "Salary must be a positive amount!")
    private BigDecimal salary;

    @Column(columnDefinition = "JSON")
    private String qualifications;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "Company cannot be null!")
    private Company company;
}
