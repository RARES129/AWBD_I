package com.awbd.awbd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCopy {

    @NotBlank(message = "Service name is required")
    @Size(max = 100, message = "Service name must be at most 100 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    @Column(nullable = false)
    private Double price;
}
