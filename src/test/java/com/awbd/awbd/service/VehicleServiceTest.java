package com.awbd.awbd.service;

import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Receipt;
import com.awbd.awbd.entity.Vehicle;
import com.awbd.awbd.exceptions.EntityInUnfinishedAppointmentException;
import com.awbd.awbd.exceptions.ResourceNotFoundException;
import com.awbd.awbd.mapper.VehicleMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ClientRepository;
import com.awbd.awbd.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    void save_shouldSaveVehicleForClient() {
        VehicleDto dto = new VehicleDto();
        Client client = new Client();
        Vehicle vehicle = new Vehicle();

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getSessionUsername).thenReturn("client1");
            when(clientRepository.findByUsername("client1")).thenReturn(client);
            when(vehicleMapper.toVehicle(dto, client)).thenReturn(vehicle);

            vehicleService.save(dto);

            verify(vehicleRepository).save(vehicle);
        }
    }

    @Test
    void findById_shouldReturnVehicleDto() {
        Vehicle vehicle = new Vehicle();
        VehicleDto dto = new VehicleDto();

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.toVehicleDto(vehicle)).thenReturn(dto);

        VehicleDto result = vehicleService.findById(1L);

        assertEquals(dto, result);
    }


    @Test
    void findById_WhenNotExists_ShouldThrow() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.findById(1L));
    }

    @Test
    void deleteById_WhenVehicleHasUnfinishedAppointments_ShouldThrow() {
        Appointment appointment = new Appointment();
        when(appointmentRepository.findByVehicle_Id(1L)).thenReturn(List.of(appointment));

        assertThrows(EntityInUnfinishedAppointmentException.class, () -> vehicleService.deleteById(1L));
    }

    @Test
    void deleteById_WhenAppointmentsFinished_ShouldDeleteVehicle() {
        Appointment appointment = new Appointment();
        appointment.setReceipt(new Receipt());
        appointment.setVehicle(new Vehicle());

        when(appointmentRepository.findByVehicle_Id(1L)).thenReturn(List.of(appointment));

        vehicleService.deleteById(1L);

        assertNull(appointment.getVehicle());
        verify(appointmentRepository).saveAll(List.of(appointment));
        verify(vehicleRepository).deleteById(1L);
    }

    @Test
    void findClientVehicles_ShouldReturnVehicleDtos() {
        Client client = new Client();
        client.setId(1L);
        Vehicle vehicle = new Vehicle();
        VehicleDto dto = new VehicleDto();

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getSessionUsername).thenReturn("client1");

            when(clientRepository.findByUsername("client1")).thenReturn(client);
            when(vehicleRepository.findByOwnerId(1L)).thenReturn(List.of(vehicle));
            when(vehicleMapper.toVehicleDto(vehicle)).thenReturn(dto);

            List<VehicleDto> result = vehicleService.findClientVehicles();

            assertEquals(1, result.size());
        }
    }

    @Test
    void findClientVehiclesPaginated_shouldReturnPagedVehicleDtos() {
        Client client = new Client();
        client.setId(1L);
        Vehicle vehicle = new Vehicle();
        VehicleDto dto = new VehicleDto();
        Pageable pageable = PageRequest.of(0, 5);
        Page<Vehicle> vehiclePage = new PageImpl<>(List.of(vehicle));

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getSessionUsername).thenReturn("client1");

            when(clientRepository.findByUsername("client1")).thenReturn(client);
            when(vehicleRepository.findByOwnerId(1L, pageable)).thenReturn(vehiclePage);
            when(vehicleMapper.toVehicleDto(vehicle)).thenReturn(dto);

            Page<VehicleDto> result = vehicleService.findClientVehiclesPaginated(pageable);

            assertEquals(1, result.getTotalElements());
            assertEquals(dto, result.getContent().getFirst());
        }
    }

    @Test
    void ensureNotInUse_WhenInUse_ShouldThrow() {
        when(appointmentRepository.existsByVehicle_Id(1L)).thenReturn(true);

        assertThrows(EntityInUnfinishedAppointmentException.class, () -> vehicleService.ensureNotInUse(1L));
    }

    @Test
    void ensureNotInUse_WhenNotInUse_ShouldNotThrow() {
        when(appointmentRepository.existsByVehicle_Id(1L)).thenReturn(false);

        assertDoesNotThrow(() -> vehicleService.ensureNotInUse(1L));
    }
}
