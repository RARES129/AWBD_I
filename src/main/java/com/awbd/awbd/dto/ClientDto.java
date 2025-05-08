package com.awbd.awbd.dto;

import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Receipt;
import com.awbd.awbd.entity.Vehicle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ClientDto extends UserDto {
//    private String clientString;
    private List<Appointment> appointments;
    private List<Vehicle> vehicles;
    private List<Receipt> receipts;
}
