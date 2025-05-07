package com.awbd.awbd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;

    @ManyToMany(mappedBy = "serviceTypes")
    private List<Mechanic> mechanics;

    @ManyToMany(mappedBy = "serviceTypes")
    private List<Appointment> appointments;
}
