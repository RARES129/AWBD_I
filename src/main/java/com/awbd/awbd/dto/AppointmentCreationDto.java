package com.awbd.awbd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreationDto {
    private LocalDateTime dateTime;
    private Long clientId;
    private Long mechanicId;
    private Long vehicleId;
    private List<Long> serviceTypeIds;
}