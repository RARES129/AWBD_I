package com.awbd.awbd.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
public class Mechanic extends User {

    @OneToMany(mappedBy = "mechanic")
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "mechanic")
    private List<ServiceType> serviceTypes;
}
