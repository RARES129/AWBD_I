package com.awbd.awbd.entity;

import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.entity.Vehicle;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Receipt> receipts;
}
