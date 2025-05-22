package com.awbd.awbd.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Issue date is required")
    @Column(nullable = false)
    private LocalDate issueDate;

    @NotNull(message = "Total amount is required")
    @Positive(message = "Total amount must be positive")
    @Column(nullable = false)
    private Double totalAmount;

    @NotBlank(message = "Vehicle info is required")
    @Size(max = 100, message = "Vehicle info must be at most 100 characters")
    @Column(nullable = false)
    private String vehicle;

    @NotNull(message = "Client is required")
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @NotNull(message = "Mechanic is required")
    @ManyToOne
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;

    @OneToOne(mappedBy = "receipt")
    private Appointment appointment;

    @ElementCollection
    @CollectionTable(name = "receipt_services", joinColumns = @JoinColumn(name = "receipt_id"))
    @NotNull(message = "Services list cannot be null")
    private List<ServiceCopy> services = new ArrayList<>();
}
