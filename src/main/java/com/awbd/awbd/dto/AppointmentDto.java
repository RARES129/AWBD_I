package com.awbd.awbd.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {
    private Long id;
    private LocalDateTime dateTime;
    private Long clientId;
    private Long mechanicId;
    private Long vehicleId;
    private List<Long> serviceTypeIds;
}
