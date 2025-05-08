package com.awbd.awbd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@DiscriminatorValue("MECHANIC")
public class Mechanic extends User {

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL)
    private List<Receipt> receipts;

    @ManyToMany
    @JoinTable(
            name = "mechanic_service_types",
            joinColumns = @JoinColumn(name = "mechanic_id"),
            inverseJoinColumns = @JoinColumn(name = "service_type_id")
    )
    private List<ServiceType> serviceTypes;
}
