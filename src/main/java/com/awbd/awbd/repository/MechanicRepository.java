package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MechanicRepository extends JpaRepository<Ticket, Long> {
}

