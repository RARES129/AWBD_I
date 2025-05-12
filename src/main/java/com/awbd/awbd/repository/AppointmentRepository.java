package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClientId(Long id);
    List<Appointment> findByMechanicId(Long id);
}

