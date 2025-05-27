package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClientIdOrderByDateTimeAsc(Long id);
    List<Appointment> findByMechanicIdOrderByDateTimeAsc(Long id);
    List<Appointment> findByServiceTypes_Id(Long serviceTypeId);
    List<Appointment> findByVehicle_Id(Long vehicleId);
    boolean existsByVehicle_Id(Long id);
}

