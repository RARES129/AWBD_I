package com.awbd.awbd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.List;

@Entity
public class Location {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private double price;
    @ManyToMany(mappedBy = "services")
    private List<Category> appointments;
}

