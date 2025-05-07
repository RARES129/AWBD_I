package com.awbd.awbd.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "organizedEvents")
    private List<Event> organizedEvents;

    @OneToMany(mappedBy = "user")
    private List<Event> tikets;

    @OneToMany(mappedBy = "user")
    private List<Reviews> reviews;
}
