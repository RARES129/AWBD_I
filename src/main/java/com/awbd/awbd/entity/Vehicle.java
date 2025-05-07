package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue
    private Long id;
    private String licensePlate;
    private String model;
    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "vehicle")
    private List<Appointment> appointments;
}

