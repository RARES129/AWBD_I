package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Appointment {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime date;
    @ManyToOne
    private Vehicle vehicle;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Mechanic mechanic;
    @ManyToMany
    private List<ServiceType> services;
}

