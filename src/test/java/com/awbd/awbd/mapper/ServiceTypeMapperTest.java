package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.ServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTypeMapperTest {

    private ServiceTypeMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ServiceTypeMapperImpl();
    }

    @Test
    void toServiceType_withValidDtoAndMechanic_mapsAllFields() {
        // Arrange
        ServiceTypeDto dto = new ServiceTypeDto();
        dto.setId(1L);
        dto.setName("Oil Change");
        dto.setPrice(100.0);

        Mechanic mechanic = new Mechanic();
        mechanic.setId(10L);
        mechanic.setUsername("john");

        // Act
        ServiceType serviceType = mapper.toServiceType(dto, mechanic);

        // Assert
        assertNotNull(serviceType);
        assertEquals(dto.getId(), serviceType.getId());
        assertEquals(dto.getName(), serviceType.getName());
        assertEquals(dto.getPrice(), serviceType.getPrice());
        assertSame(mechanic, serviceType.getMechanic());
    }

    @Test
    void toServiceType_withNullDto_returnsNull() {
        // Act
        ServiceType serviceType = mapper.toServiceType(null, new Mechanic());

        // Assert
        assertNull(serviceType);
    }

    @Test
    void toServiceTypeDto_withValidServiceType_mapsAllFields() {
        // Arrange
        ServiceType serviceType = new ServiceType();
        serviceType.setId(2L);
        serviceType.setName("Tire Replacement");
        serviceType.setPrice(250.0);

        // Act
        ServiceTypeDto dto = mapper.toServiceTypeDto(serviceType);

        // Assert
        assertNotNull(dto);
        assertEquals(serviceType.getId(), dto.getId());
        assertEquals(serviceType.getName(), dto.getName());
        assertEquals(serviceType.getPrice(), dto.getPrice());
    }

    @Test
    void toServiceTypeDto_withNullServiceType_returnsNull() {
        // Act
        ServiceTypeDto dto = mapper.toServiceTypeDto(null);

        // Assert
        assertNull(dto);
    }
}
