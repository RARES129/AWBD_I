package com.awbd.awbd.service;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Receipt;
import com.awbd.awbd.entity.Vehicle;
import com.awbd.awbd.mapper.VehicleMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ClientRepository;
import com.awbd.awbd.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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

        when(vehicleRepository.findVehicleById(1L)).thenReturn(vehicle);
        when(vehicleMapper.toVehicleDto(vehicle)).thenReturn(dto);

        VehicleDto result = vehicleService.findById(1L);

        assertEquals(dto, result);
    }

    @Test
    void deleteById_shouldThrowIfVehicleHasUnfinishedAppointments() {
        Appointment unfinished = new Appointment(); // no receipt == unfinished
        List<Appointment> appointments = List.of(unfinished);

        when(appointmentRepository.findByVehicle_Id(1L)).thenReturn(appointments);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> vehicleService.deleteById(1L));
        assertEquals("Vehicle is used in an unfinished appointment.", ex.getMessage());
    }

    @Test
    void deleteById_shouldDeleteIfNoUnfinishedAppointments() {
        Appointment finished = new Appointment();
        finished.setReceipt(new Receipt()); // assume not null means finished
        finished.setVehicle(new Vehicle());

        List<Appointment> appointments = List.of(finished);

        when(appointmentRepository.findByVehicle_Id(1L)).thenReturn(appointments);

        vehicleService.deleteById(1L);

        verify(appointmentRepository).saveAll(anyList());
        verify(vehicleRepository).deleteById(1L);
        assertNull(finished.getVehicle());
    }

    @Test
    void findClientVehicles_shouldReturnVehicleDtos() {
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
    void ensureNotInUse_shouldThrowIfUsed() {
        when(appointmentRepository.existsByVehicle_Id(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> vehicleService.ensureNotInUse(1L));
        assertEquals("Vehicle is used in an unfinished appointment.", ex.getMessage());
    }

    @Test
    void ensureNotInUse_shouldNotThrowIfNotUsed() {
        when(appointmentRepository.existsByVehicle_Id(1L)).thenReturn(false);

        assertDoesNotThrow(() -> vehicleService.ensureNotInUse(1L));
    }
}
