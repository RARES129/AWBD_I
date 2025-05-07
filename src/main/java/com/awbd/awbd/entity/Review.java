package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Review {
    @Id @GeneratedValue
    private Long id;

    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;
}
