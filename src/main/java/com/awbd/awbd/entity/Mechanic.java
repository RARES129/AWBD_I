package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Mechanic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String specialization;
    @OneToOne
    private User user;
    @OneToMany(mappedBy = "mechanic")
    private List<Appointment> appointments;
}

