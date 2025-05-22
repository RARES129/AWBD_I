package com.awbd.awbd.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
public class Mechanic extends User {

    @OneToMany(mappedBy = "mechanic")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceType> serviceTypes = new ArrayList<>();
}
