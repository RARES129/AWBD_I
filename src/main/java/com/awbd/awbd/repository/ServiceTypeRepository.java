package com.awbd.awbd.repository;

import com.awbd.awbd.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    List<ServiceType> findByMechanicId(Long mechanicId);
}

