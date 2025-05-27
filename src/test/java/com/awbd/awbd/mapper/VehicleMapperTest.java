package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleMapperTest {

    private VehicleMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new VehicleMapperImpl();
    }

    @Test
    void toVehicle_withValidDtoAndClient_mapsAllFields() {
        // Arrange
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(1L);
        vehicleDto.setBrand("Toyota");
        vehicleDto.setModel("Corolla");
        vehicleDto.setPlateNumber("B123ABC");

        Client client = Client.builder()
                .id(10L)
                .username("client1")
                .password("pass123")
                .build();

        // Act
        Vehicle vehicle = mapper.toVehicle(vehicleDto, client);

        // Assert
        assertNotNull(vehicle);
        assertEquals(vehicleDto.getId(), vehicle.getId());
        assertEquals(vehicleDto.getBrand(), vehicle.getBrand());
        assertEquals(vehicleDto.getModel(), vehicle.getModel());
        assertEquals(vehicleDto.getPlateNumber(), vehicle.getPlateNumber());
        assertSame(client, vehicle.getOwner());
    }

    @Test
    void toVehicle_withNullDto_returnsNull() {
        Client client = new Client();
        Vehicle result = mapper.toVehicle(null, client);
        assertNull(result);
    }

    @Test
    void toVehicleDto_withValidVehicle_mapsAllFields() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setId(2L);
        vehicle.setBrand("BMW");
        vehicle.setModel("X5");
        vehicle.setPlateNumber("CJ45XYZ");

        // Act
        VehicleDto dto = mapper.toVehicleDto(vehicle);

        // Assert
        assertNotNull(dto);
        assertEquals(vehicle.getId(), dto.getId());
        assertEquals(vehicle.getBrand(), dto.getBrand());
        assertEquals(vehicle.getModel(), dto.getModel());
        assertEquals(vehicle.getPlateNumber(), dto.getPlateNumber());
    }

    @Test
    void toVehicleDto_withNullVehicle_returnsNull() {
        VehicleDto result = mapper.toVehicleDto(null);
        assertNull(result);
    }
}
