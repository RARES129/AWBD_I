package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Review {
    @Id
    @GeneratedValue
    private Long id;
    private String licensePlate;
    private String model;
    @ManyToOne
    private Event client;
    @OneToMany(mappedBy = "vehicle")
    private List<Category> appointments;
}

