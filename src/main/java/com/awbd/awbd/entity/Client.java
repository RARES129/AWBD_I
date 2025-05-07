package com.awbd.awbd.entity;

import jakarta.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

