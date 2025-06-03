package com.awbd.awbd.controller;

import com.awbd.awbd.entity.*;
import com.awbd.awbd.exceptions.EntityInUnfinishedAppointmentException;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import com.awbd.awbd.repository.UserRepository;
import com.awbd.awbd.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class GlobalExceptionHandlerTest {

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

    @InjectMocks
    private GlobalExceptionHandler handler;

    private Long vehicleId;
    private Long serviceTypeId;

    @BeforeEach
    void setUp() {
        Client client = userRepository.save(Client.builder()
                .username("client")
                .password("12345")
                .role(Role.CLIENT)
                .build());

        Mechanic mechanic = userRepository.save(Mechanic.builder()
                .username("mechanic")
                .password("12345")
                .role(Role.MECHANIC)
                .build());

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

        appointmentRepository.save(Appointment.builder()
                .client(client)
                .mechanic(mechanic)
                .vehicle(vehicle)
                .dateTime(LocalDateTime.now().plusDays(1))
                .serviceTypes(new ArrayList<>(List.of(serviceType)))
                .build());
    }

    @Test
    @WithUserDetails(value = "mechanic", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testHandleResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/service-type/edit/9999"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("notFoundException"))
                .andExpect(model().attributeExists("exception"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testHandleEntityInUnfinishedAppointmentException_Vehicle() throws Exception {
        mockMvc.perform(get("/vehicle/delete/{id}", vehicleId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vehicle"))
                .andExpect(flash().attribute("errorMessage", containsString("is used in an unfinished appointment")));
    }

    @Test
    @WithMockUser(username = "mechanic", roles = "MECHANIC")
    void testHandleEntityInUnfinishedAppointmentException_ServiceType() throws Exception {
        mockMvc.perform(get("/service-type/delete/{id}", serviceTypeId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/service-type"))
                .andExpect(flash().attribute("errorMessage", containsString("is used in an unfinished appointment")));
    }

    @Test
    void testHandleIllegalArgumentWithViewException() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "client")
                        .param("password", "12345")
                        .param("role", "CLIENT")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testHandleEntityInUse_UnknownEntity() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        EntityInUnfinishedAppointmentException ex =
                new EntityInUnfinishedAppointmentException("Other");

        String result = handler.handleEntityInUse(ex, redirectAttributes);
        assertEquals("redirect:/", result);
    }
}
