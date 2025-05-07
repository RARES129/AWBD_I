package com.awbd.awbd.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Ticket {
    @Id @GeneratedValue
    private Long id;

    private LocalDateTime issueDate;
    private BigDecimal price;
    private String qrCode;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    private User user;

    @ManyToOne
    private Event event;
}

