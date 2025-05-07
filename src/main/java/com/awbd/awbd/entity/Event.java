package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class Event {
    @Id @GeneratedValue
    private Long id;

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isPublic;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ManyToOne
    private Location location;

    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "event")
    private List<Review> reviews;

    @ManyToMany(mappedBy = "joinedEvents")
    private Set<User> participants;
}


