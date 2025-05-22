package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findVehicleById(long id);
    List<Vehicle> findByOwnerId(Long id);
}

