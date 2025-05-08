package com.awbd.awbd.repository;

import com.awbd.awbd.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    List<Receipt> findByClientId(Long clientId);
    List<Receipt> findByMechanicId(Long mechanicId);
    List<Receipt> findByAppointmentId(Long appointmentId);
}