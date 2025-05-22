package com.awbd.awbd.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Long id;

    @NotNull(message = "Appointment date and time is required")
    @FutureOrPresent(message = "Appointment date and time must be present or future")
    private LocalDateTime dateTime;

    private Long clientId;

    @NotNull(message = "Mechanic ID is required")
    private Long mechanicId;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    @NotEmpty(message = "At least one service type ID is required")
    private List<Long> serviceTypeIds;
}
