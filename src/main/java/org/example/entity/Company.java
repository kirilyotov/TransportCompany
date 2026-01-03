package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.validator.InvalidNames;
import org.example.validator.ValidPhoneNumber;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "companies",
        indexes = {
        @Index(name = "idx_company_name", columnList = "name")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(callSuper = true, exclude = {"vehicles", "employees", "clients"})
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer id;
    
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
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Vehicle> vehicles = new HashSet<>();
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Employee> employees = new HashSet<>();
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Client> clients = new HashSet<>();
}
