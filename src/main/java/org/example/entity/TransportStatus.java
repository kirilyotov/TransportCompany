package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transport_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
public class TransportStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transport_id", nullable = false)
    @NotNull(message = "Transport cannot be null!")
    private Transport transport;

    @Column(nullable = false)
    @NotNull(message = "Status change datetime cannot be null!")
    private LocalDateTime statusChange;

    @Column(length = 50)
    @NotBlank(message = "Old status cannot be blank!")
    private String oldStatus;

    @Column(length = 50)
    @NotBlank(message = "New status cannot be blank!")
    private String newStatus;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
