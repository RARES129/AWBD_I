package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Category {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Event> events;
}

