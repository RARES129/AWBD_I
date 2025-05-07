package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Mechanic extends User{

    private String name;
    private String specialization;
    @OneToMany(mappedBy = "mechanic")
    private List<Appointment> appointments;
}

