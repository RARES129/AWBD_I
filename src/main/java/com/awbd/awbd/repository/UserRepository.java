package com.awbd.awbd.repository;

import com.awbd.awbd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}

