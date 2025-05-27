package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptMapperTest {

    private ReceiptMapper receiptMapper;

    @BeforeEach
    void setUp() {
        receiptMapper = Mappers.getMapper(ReceiptMapper.class);
    }

    @Test
    void testToReceiptDto_allFieldsMapped_correctly() {
        Client client = new Client();
        client.setId(1L);
        client.setUsername("clientUser");

        Mechanic mechanic = new Mechanic();
        mechanic.setId(2L);
        mechanic.setUsername("mechanicUser");

        Appointment appointment = new Appointment();
        appointment.setId(3L);
        appointment.setDateTime(LocalDateTime.of(2025, 5, 26, 15, 30));

        List<ServiceCopy> services = new ArrayList<>();
        ServiceCopy s1 = new ServiceCopy(); // assuming empty constructor + setters
        services.add(s1);

        Receipt receipt = new Receipt();
        receipt.setId(10L);
        receipt.setClient(client);
        receipt.setMechanic(mechanic);
        receipt.setAppointment(appointment);
        receipt.setIssueDate(LocalDate.from(LocalDateTime.of(2025, 5, 26, 16, 0)));
        receipt.setTotalAmount(100.0);
        receipt.setVehicle("Toyota");
        receipt.setServices(services);

        ReceiptDto dto = receiptMapper.toReceiptDto(receipt);

        assertNotNull(dto);
        assertEquals(client.getId(), dto.getClientId());
        assertEquals(client.getUsername(), dto.getClientUsername());
        assertEquals(mechanic.getId(), dto.getMechanicId());
        assertEquals(mechanic.getUsername(), dto.getMechanicUsername());
        assertEquals(appointment.getId(), dto.getAppointmentId());
        assertEquals(appointment.getDateTime(), dto.getAppointmentDateTime());
        assertEquals(receipt.getId(), dto.getId());
        assertEquals(receipt.getIssueDate(), dto.getIssueDate());
        assertEquals(receipt.getTotalAmount(), dto.getTotalAmount());
        assertEquals(receipt.getVehicle(), dto.getVehicle());
        assertNotNull(dto.getServices());
        assertEquals(1, dto.getServices().size());
        assertNotSame(services, dto.getServices()); // ensure copy, not same list reference
    }

    @Test
    void testToReceiptDto_nullReceipt_returnsNull() {
        assertNull(receiptMapper.toReceiptDto(null));
    }

    @Test
    void testToReceiptDto_nullNestedClientMechanicAppointment() {
        Receipt receipt = new Receipt();
        receipt.setId(11L);
        // no client, mechanic or appointment set (all null)
        receipt.setServices(null);

        ReceiptDto dto = receiptMapper.toReceiptDto(receipt);

        assertNotNull(dto);
        assertEquals(11L, dto.getId());
        assertNull(dto.getClientId());
        assertNull(dto.getClientUsername());
        assertNull(dto.getMechanicId());
        assertNull(dto.getMechanicUsername());
        assertNull(dto.getAppointmentId());
        assertNull(dto.getAppointmentDateTime());
        assertNull(dto.getServices()); // services list null in entity means null in DTO
    }

    @Test
    void testToReceiptDto_emptyServicesList() {
        Receipt receipt = new Receipt();
        receipt.setServices(new ArrayList<>());

        ReceiptDto dto = receiptMapper.toReceiptDto(receipt);

        assertNotNull(dto);
        assertNotNull(dto.getServices());
        assertTrue(dto.getServices().isEmpty());
    }
}
