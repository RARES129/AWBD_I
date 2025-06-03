package com.awbd.awbd.controller;

import com.awbd.awbd.entity.*;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import com.awbd.awbd.repository.UserRepository;
import com.awbd.awbd.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private Long mechanicId;
    private Long vehicleId;
    private Long serviceTypeId;
    private Long appointmentId;
    private Long appointmentWithoutReceiptId;

    @BeforeEach
    void setUp() {
        Client client = Client.builder()
                .username("client")
                .password("12345")
                .role(Role.CLIENT)
                .build();
        client = userRepository.save(client);

        Mechanic mechanic = Mechanic.builder()
                .username("mechanic")
                .password("12345")
                .role(Role.MECHANIC)
                .build();
        mechanic = userRepository.save(mechanic);
        mechanicId = mechanic.getId();

        Vehicle vehicle = Vehicle.builder()
                .brand("Toyota")
                .model("Corolla")
                .plateNumber("AB123CD")
                .owner(client)
                .build();
        vehicle = vehicleRepository.save(vehicle);
        vehicleId = vehicle.getId();

        ServiceType serviceType = ServiceType.builder()
                .name("Oil Change")
                .price(79.99)
                .mechanic(mechanic)
                .build();
        serviceType = serviceTypeRepository.save(serviceType);
        serviceTypeId = serviceType.getId();

        Appointment appointment = Appointment.builder()
                .client(client)
                .mechanic(mechanic)
                .vehicle(vehicle)
                .dateTime(LocalDateTime.now().plusDays(1))
                .serviceTypes(new ArrayList<>(List.of(serviceType)))
                .build();
        appointment = appointmentRepository.save(appointment);
        appointmentId = appointment.getId();

        Receipt receipt = Receipt.builder()
                .issueDate(LocalDate.now())
                .totalAmount(serviceType.getPrice())
                .client(client)
                .mechanic(mechanic)
                .appointment(appointment)
                .vehicle(vehicle.getBrand() + " " + vehicle.getModel() + " " + vehicle.getPlateNumber())
                .services(new ArrayList<>(List.of(ServiceCopy.builder()
                        .price(serviceType.getPrice())
                        .name(serviceType.getName())
                        .build())))
                .build();

        appointment.setReceipt(receipt);
        appointmentRepository.save(appointment);

        Appointment appointmentWithoutReceipt = Appointment.builder()
                .client(client)
                .mechanic(mechanic)
                .vehicle(vehicle)
                .dateTime(LocalDateTime.now().plusDays(1))
                .serviceTypes(new ArrayList<>(List.of(serviceType)))
                .build();
        appointmentWithoutReceipt = appointmentRepository.save(appointmentWithoutReceipt);
        appointmentWithoutReceiptId = appointmentWithoutReceipt.getId();
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAppointmentForm() throws Exception {
        mockMvc.perform(get("/appointment/mechanic/{mechanicId}", mechanicId))
                .andExpect(status().isOk())
                .andExpect(view().name("appointmentForm"))
                .andExpect(model().attributeExists("appointment"))
                .andExpect(model().attributeExists("vehicles"))
                .andExpect(model().attributeExists("serviceTypes"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testSaveOrUpdate_Valid() throws Exception {
        mockMvc.perform(post("/appointment")
                        .param("mechanicId", mechanicId.toString())
                        .param("vehicleId", vehicleId.toString())
                        .param("dateTime", LocalDateTime.now().plusDays(1).toString())
                        .param("serviceTypeIds[0]", serviceTypeId.toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointment"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testSaveOrUpdate_WithValidationErrors() throws Exception {
        mockMvc.perform(post("/appointment")
                        .param("mechanicId", mechanicId.toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("appointmentForm"))
                .andExpect(model().attributeExists("vehicles"))
                .andExpect(model().attributeExists("serviceTypes"))
                .andExpect(model().attributeExists("appointment"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAppointmentList() throws Exception {
        mockMvc.perform(get("/appointment"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointmentList"))
                .andExpect(model().attributeExists("appointments"));
    }

    @Test
    @WithUserDetails(value = "mechanic", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testGenerateReceipt() throws Exception {
        mockMvc.perform(post("/appointment/{appointmentId}/receipt", appointmentWithoutReceiptId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointment"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testViewReceipt() throws Exception {
        mockMvc.perform(get("/appointment/receipt/{appointmentId}", appointmentId))
                .andExpect(status().isOk())
                .andExpect(view().name("receiptView"))
                .andExpect(model().attributeExists("receipt"));
    }
}
