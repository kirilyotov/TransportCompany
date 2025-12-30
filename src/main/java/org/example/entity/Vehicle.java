package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.entity.enums.VehicleStatus;
import org.example.entity.enums.VehicleType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "vehicles",
        indexes = {
                @Index(name = "idx_vehicle_license_plate", columnList = "license_plate"),
                @Index(name = "idx_vehicle_type", columnList = "type"),
                @Index(name = "idx_vehicle_status", columnList = "status")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true, exclude = {"transports"})
public class Vehicle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private int id;

    @Column(name = "license_plate", nullable = false, unique = true, length = 20)
    @NotBlank(message = "License plate cannot be blank!")
    private String licensePlate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @NotNull(message = "Vehicle type cannot be null!")
    private VehicleType type;

    @Column(name = "capacity_weight", precision = 8, scale = 2)
    private BigDecimal capacityWeight;

    @Column(name = "capacity_passengers")
    private Integer capacityPassengers;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "Company cannot be null!")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VehicleStatus status;
    
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Transport> transports = new HashSet<>();
}
