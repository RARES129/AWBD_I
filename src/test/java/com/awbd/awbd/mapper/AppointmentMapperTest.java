package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.entity.*;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import com.awbd.awbd.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentMapperTest {

    private AppointmentMapper mapper;

    private MechanicRepository mechanicRepository;
    private VehicleRepository vehicleRepository;
    private ServiceTypeRepository serviceTypeRepository;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AppointmentMapper.class);
        mechanicRepository = mock(MechanicRepository.class);
        vehicleRepository = mock(VehicleRepository.class);
        serviceTypeRepository = mock(ServiceTypeRepository.class);
    }

    @Test
    void testToAppointment_successfulMapping() {
        // Arrange
        AppointmentDto dto = new AppointmentDto();
        dto.setId(1L);
        dto.setMechanicId(10L);
        dto.setVehicleId(20L);
        dto.setServiceTypeIds(List.of(30L, 31L));

        Client client = new Client();
        client.setId(100L);

        Mechanic mechanic = new Mechanic();
        mechanic.setId(10L);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(20L);

        ServiceType st1 = new ServiceType();
        st1.setId(30L);

        ServiceType st2 = new ServiceType();
        st2.setId(31L);

        when(mechanicRepository.findById(10L)).thenReturn(Optional.of(mechanic));
        when(vehicleRepository.findById(20L)).thenReturn(Optional.of(vehicle));
        when(serviceTypeRepository.findById(30L)).thenReturn(Optional.of(st1));
        when(serviceTypeRepository.findById(31L)).thenReturn(Optional.of(st2));

        // Act
        Appointment result = mapper.toAppointment(dto, client, mechanicRepository, vehicleRepository, serviceTypeRepository);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(client, result.getClient());
        assertEquals(mechanic, result.getMechanic());
        assertEquals(vehicle, result.getVehicle());
        assertEquals(2, result.getServiceTypes().size());
        assertTrue(result.getServiceTypes().contains(st1));
        assertTrue(result.getServiceTypes().contains(st2));
    }

    @Test
    void testToAppointment_missingVehicle_throwsException() {
        // Arrange
        AppointmentDto dto = new AppointmentDto();
        dto.setId(1L);
        dto.setVehicleId(999L);

        Client client = new Client();

        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                mapper.toAppointment(dto, client, mechanicRepository, vehicleRepository, serviceTypeRepository));
    }

    @Test
    void testToAppointment_nullServiceTypeIdIsHandled() {
        // Arrange
        AppointmentDto dto = new AppointmentDto();
        List<Long> serviceTypeIds = new java.util.ArrayList<>();
        serviceTypeIds.add(30L);
        serviceTypeIds.add(null);
        dto.setServiceTypeIds(serviceTypeIds);

        Client client = new Client();
        Vehicle vehicle = new Vehicle();
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));

        ServiceType st1 = new ServiceType();
        st1.setId(30L);
        when(serviceTypeRepository.findById(30L)).thenReturn(Optional.of(st1));

        // Act
        Appointment result = mapper.toAppointment(dto, client, mechanicRepository, vehicleRepository, serviceTypeRepository);

        // Assert
        assertNotNull(result.getServiceTypes());
        assertEquals(2, result.getServiceTypes().size());
        assertTrue(result.getServiceTypes().contains(null));
    }

    @Test
    void testToAppointment_nullServiceTypeIds_returnsNull() {
        // Arrange
        AppointmentDto dto = new AppointmentDto();
        dto.setId(1L);
        dto.setVehicleId(20L);
        dto.setMechanicId(10L);
        dto.setServiceTypeIds(null); // explicitly null

        Client client = new Client();
        Mechanic mechanic = new Mechanic();
        mechanic.setId(10L);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(20L);

        when(mechanicRepository.findById(10L)).thenReturn(Optional.of(mechanic));
        when(vehicleRepository.findById(20L)).thenReturn(Optional.of(vehicle));

        // Act
        Appointment result = mapper.toAppointment(dto, client, mechanicRepository, vehicleRepository, serviceTypeRepository);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(client, result.getClient());
        assertEquals(mechanic, result.getMechanic());
        assertEquals(vehicle, result.getVehicle());
        assertNull(result.getServiceTypes(), "Expected null serviceTypes when serviceTypeIds is null");
    }

    @Test
    void testToAppointment_bothDtoAndClientNull_returnsNull() {
        // Act
        Appointment result = mapper.toAppointment(null, null, mechanicRepository, vehicleRepository, serviceTypeRepository);

        // Assert
        assertNull(result, "Expected null when both appointmentDto and client are null");
    }

    @Test
    void testToAppointment_nullDtoButNonNullClient_returnsAppointmentWithOnlyClientSet() {
        // Arrange
        Client client = new Client();
        client.setId(123L);

        // Act
        Appointment result = mapper.toAppointment(null, client, mechanicRepository, vehicleRepository, serviceTypeRepository);

        // Assert
        assertNotNull(result);
        assertNull(result.getId());
        assertEquals(client, result.getClient());
        assertNull(result.getMechanic());
        assertNull(result.getVehicle());
        assertTrue(result.getServiceTypes() == null || result.getServiceTypes().isEmpty(), "Service types should be null or empty");
        assertNull(result.getDateTime());
    }
}
