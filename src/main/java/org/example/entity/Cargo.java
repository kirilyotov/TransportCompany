package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.entity.enums.CargoType;
import org.example.validator.ValidAmount;

import java.math.BigDecimal;

@Entity
@Table(name = "cargo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Cargo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cargo_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transport_id", nullable = false)
    @NotNull(message = "Transport cannot be null!")
    private Transport transport;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Cargo type cannot be null!")
    private CargoType type;

    @Column(length = 255)
    private String description;

    @Column(precision = 8, scale = 2)
    @ValidAmount(message = "Total weight must be positive if specified!")
    private BigDecimal totalWeight;

    @Column(name = "passenger_count")
    private Integer passengerCount;
}
