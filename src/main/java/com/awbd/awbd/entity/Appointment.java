package com.awbd.awbd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Appointment date and time is required")
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @NotNull(message = "Client is required")
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull(message = "Mechanic is required")
    @ManyToOne
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;

    @NotNull(message = "Vehicle is required")
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @NotNull(message = "At least one service type is required")
    @ManyToMany
    @JoinTable(
            name = "appointment_service_type",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_type_id")
    )
    private List<ServiceType> serviceTypes = new ArrayList<>();

    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;
}
