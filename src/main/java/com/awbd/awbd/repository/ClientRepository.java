package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Event, Long> {
}

