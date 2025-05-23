package com.awbd.awbd.service;

import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.entity.*;
import com.awbd.awbd.mapper.ReceiptMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReceiptServiceTest {

    private ReceiptRepository receiptRepository;
    private AppointmentRepository appointmentRepository;
    private ReceiptMapper receiptMapper;
    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        receiptRepository = mock(ReceiptRepository.class);
        appointmentRepository = mock(AppointmentRepository.class);
        receiptMapper = mock(ReceiptMapper.class);
        receiptService = new ReceiptService(receiptRepository, appointmentRepository, receiptMapper);
    }

    @Test
    void generateReceiptForAppointment_ShouldCreateAndSaveReceipt() {
        // Arrange
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

        when(appointmentRepository.findAppointmentById(appointmentId)).thenReturn(appointment);

        // Act
        receiptService.generateReceiptForAppointment(appointmentId);

        // Assert
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
    void getReceiptByAppointmentId_ShouldReturnReceiptDto() {
        // Arrange
        Long appointmentId = 1L;
        Receipt receipt = new Receipt();
        ReceiptDto receiptDto = new ReceiptDto();

        when(receiptRepository.findByAppointmentId(appointmentId)).thenReturn(receipt);
        when(receiptMapper.toReceiptDto(receipt)).thenReturn(receiptDto);

        // Act
        ReceiptDto result = receiptService.getReceiptByAppointmentId(appointmentId);

        // Assert
        assertNotNull(result);
        assertEquals(receiptDto, result);
    }
}
