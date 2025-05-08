package com.awbd.awbd.dto;

import lombok.Data;

@Data
public class VehicleDto {
    private Long id;

    private String brand;
    private String model;
    private String plateNumber;
}
