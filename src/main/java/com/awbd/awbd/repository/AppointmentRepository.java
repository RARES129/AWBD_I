package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}

