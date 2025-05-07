package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Review, Long> {
}

