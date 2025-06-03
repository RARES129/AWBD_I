package com.awbd.awbd.service;

import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.entity.*;
import com.awbd.awbd.exceptions.ResourceNotFoundException;
import com.awbd.awbd.mapper.ReceiptMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @InjectMocks
    private ReceiptService receiptService;

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ReceiptMapper receiptMapper;

    @BeforeEach
    void setUp() {
        receiptRepository = mock(ReceiptRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        receiptMapper = mock(ReceiptMapper.class);
        receiptService = new ReceiptService(receiptRepository, appointmentRepository, receiptMapper);
    }

    @Test
    void generateReceiptForAppointment_ShouldCreateAndSaveReceipt() {
        Long appointmentId = 1L;
        Client client = new Client();
        Mechanic mechanic = new Mechanic();
        Vehicle vehicle = new Vehicle();
        vehicle.setBrand("BMW");
        vehicle.setModel("X5");
        vehicle.setPlateNumber("AB-12-XYZ");

        ServiceType service1 = new ServiceType();
        service1.setName("Oil Change");
        service1.setPrice(150.0);

        ServiceType service2 = new ServiceType();
        service2.setName("Brake Check");
        service2.setPrice(200.0);

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setClient(client);
        appointment.setMechanic(mechanic);
        appointment.setVehicle(vehicle);
        appointment.setServiceTypes(List.of(service1, service2));

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        receiptService.generateReceiptForAppointment(appointmentId);

        ArgumentCaptor<Appointment> appointmentCaptor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(appointmentCaptor.capture());

        Appointment savedAppointment = appointmentCaptor.getValue();
        Receipt savedReceipt = savedAppointment.getReceipt();

        assertNotNull(savedReceipt);
        assertEquals(client, savedReceipt.getClient());
        assertEquals(mechanic, savedReceipt.getMechanic());
        assertEquals(appointment, savedReceipt.getAppointment());
        assertEquals("BMW X5 AB-12-XYZ", savedReceipt.getVehicle());
        assertEquals(2, savedReceipt.getServices().size());
        assertEquals(350.0, savedReceipt.getTotalAmount());
        assertEquals(LocalDate.now(), savedReceipt.getIssueDate());
    }

    @Test
    void generateReceiptForAppointment_ShouldThrowException_WhenAppointmentNotFound() {
        Long nonExistentAppointmentId = 999L;
        when(appointmentRepository.findById(nonExistentAppointmentId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> receiptService.generateReceiptForAppointment(nonExistentAppointmentId));

        assertEquals("Appointment not found with id: 999", ex.getMessage());
    }

    @Test
    void getReceiptByAppointmentId_ShouldReturnReceiptDto() {
        Long appointmentId = 1L;
        Receipt receipt = new Receipt();
        ReceiptDto receiptDto = new ReceiptDto();

        when(receiptRepository.findByAppointmentId(appointmentId)).thenReturn(receipt);
        when(receiptMapper.toReceiptDto(receipt)).thenReturn(receiptDto);

        ReceiptDto result = receiptService.getReceiptByAppointmentId(appointmentId);

        assertNotNull(result);
        assertEquals(receiptDto, result);
    }
}
