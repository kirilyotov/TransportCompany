package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.validator.InvalidNames;
import org.example.validator.ValidEmail;
import org.example.validator.ValidPhoneNumber;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "clients",
        indexes = {
        @Index(name = "idx_client_name", columnList = "name"),
        @Index(name = "idx_client_surname", columnList = "surname"),
        @Index(name = "idx_client_company", columnList = "company_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(callSuper = true, exclude = {"transports", "payments"})
public class Client extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private int id;

    @Column(nullable = false, length = 50)
    @InvalidNames(message = "The name is not valid!")
    @NotBlank(message = "Client name cannot be blank!")
    @Size(max = 50, message = "Client name has to be with up to 50 characters!")
    private String name;

    @Column(nullable = false, length = 50)
    @InvalidNames(message = "The surname is not valid!")
    @NotBlank(message = "Surname cannot be blank!")
    @Size(max = 50, message = "Surname has to be with up to 50 characters!")
    private String surname;

    @Column(length = 20)
    @ValidPhoneNumber(message = "Invalid phone number format!")
    private String phone;
    
    @Column(unique = true, nullable = false)
    @ValidEmail(message = "Invalid email format!")
    private String email;
    
    @Column(length = 255)
    private String address;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Transport> transports = new HashSet<>();
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Payment> payments = new HashSet<>();
}
