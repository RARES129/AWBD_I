package com.awbd.awbd.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Mechanic extends User {

    @ToString.Exclude
    @OneToMany(mappedBy = "mechanic")
    private List<Appointment> appointments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceType> serviceTypes = new ArrayList<>();
}
