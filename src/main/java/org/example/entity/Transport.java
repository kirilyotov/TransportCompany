package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.entity.enums.TransportStatus;
import org.example.validator.ValidAmount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transports",
        indexes = {
                @Index(name = "idx_transport_destination", columnList = "end_point"),
                @Index(name = "idx_transport_dates", columnList = "departure_date, arrival_date"),
                @Index(name = "idx_transport_company", columnList = "company_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class Transport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transport_id")
    private int id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Start point cannot be blank!")
    private String startPoint;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "End point cannot be blank!")
    private String endPoint;

    @Column(nullable = false)
    @NotNull(message = "Departure date cannot be null!")
    private LocalDateTime departureDate;

    @Column()
    private LocalDateTime arrivalDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @NotNull(message = "Vehicle cannot be null!")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    @NotNull(message = "Driver cannot be null!")
    private Employee driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Client cannot be null!")
    private Client client;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Price cannot be null!")
    @ValidAmount(message = "Price must be a positive amount!")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @NotNull(message = "Company cannot be null!")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransportStatus status;
}
