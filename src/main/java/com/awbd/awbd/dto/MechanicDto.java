package com.awbd.awbd.dto;

import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Receipt;
import com.awbd.awbd.entity.ServiceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MechanicDto extends UserDto {
//    private String mechanicString;
    private List<AppointmentDto> appointments;
    private List<ReceiptDto> receipts;
    private List<ServiceTypeDto> serviceTypes;
}
