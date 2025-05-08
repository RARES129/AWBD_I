package com.awbd.awbd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDto {
    private Long id;
    private LocalDate issueDate;
    private Double totalAmount;
    private Long clientId;
    private Long mechanicId;
    private Long appointmentId;
}
