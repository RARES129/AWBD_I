package com.awbd.awbd.dto;

import com.awbd.awbd.entity.ServiceCopy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDto {
    private Long id;
    private LocalDate issueDate;
    private Double totalAmount;

    private Long clientId;
    private String clientUsername;

    private Long mechanicId;
    private String mechanicUsername;

    private String vehicle;

    private Long appointmentId;
    private LocalDateTime appointmentDateTime;

    private List<ServiceCopy> services;
}
