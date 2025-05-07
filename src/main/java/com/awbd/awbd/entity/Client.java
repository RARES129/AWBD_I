package com.awbd.awbd.entity;

import jakarta.persistence.*;

@Entity
public class Client extends User {
    private String name;
    private String phone;

}

