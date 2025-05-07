package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentRepository extends JpaRepository<Category, Long> {
}

