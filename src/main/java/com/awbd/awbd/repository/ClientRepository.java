package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByUsername(String username);
}

