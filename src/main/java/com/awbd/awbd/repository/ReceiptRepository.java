package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Receipt findByAppointmentId(Long appointmentId);
}