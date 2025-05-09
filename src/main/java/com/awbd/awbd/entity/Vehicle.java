package com.awbd.awbd.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String plateNumber;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client owner;
}
