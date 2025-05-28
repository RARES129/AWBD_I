package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByOwnerId(Long id);
    Page<Vehicle> findByOwnerId(Long id, Pageable pageable);
}

