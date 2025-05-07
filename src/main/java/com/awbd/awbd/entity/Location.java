package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Location {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String address;
    private String city;
    private int capacity;

    @OneToMany(mappedBy = "location")
    private List<Event> events;
}


