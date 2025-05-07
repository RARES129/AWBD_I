package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime date;
    @ManyToOne
    private Review vehicle;
    @ManyToOne
    private Event client;
    @ManyToOne
    private Ticket mechanic;
    @ManyToMany
    private List<Location> services;
}

