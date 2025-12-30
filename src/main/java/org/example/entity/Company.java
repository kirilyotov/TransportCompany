package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.validator.InvalidNames;
import org.example.validator.ValidPhoneNumber;

@Entity
@Table(name = "companies",
        indexes = {
        @Index(name = "idx_company_name", columnList = "name")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private int id;
    
    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "Company name cannot be blank!")
    @Size(max = 100, message = "Company name has to be with up to 100 characters!")
    @InvalidNames(message = "Company name is not valid!")
    private String name;
    
    @Column(length = 255)
    private String address;
    
    @Column(length = 20)
    @ValidPhoneNumber(message = "Invalid phone number format!")
    private String phone;
}
