package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServiceTypeRepository extends JpaRepository<Location, Long> {
}

