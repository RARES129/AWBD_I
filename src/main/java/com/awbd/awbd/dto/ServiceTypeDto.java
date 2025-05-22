package com.awbd.awbd.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceTypeDto {
    private Long id;

    @NotBlank(message = "Service name is required")
    @Size(max = 100, message = "Service name must be at most 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private Double price;
}
