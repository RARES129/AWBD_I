package com.awbd.awbd.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@NoArgsConstructor
@SuperBuilder
public class Client extends User {

    @OneToMany(mappedBy = "owner")
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "client")
    private List<Appointment> appointments;
}
