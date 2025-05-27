package com.awbd.awbd.controller;

import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.dto.ReceiptDto;
import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.dto.VehicleDto;
import com.awbd.awbd.service.AppointmentService;
import com.awbd.awbd.service.ReceiptService;
import com.awbd.awbd.service.ServiceTypeService;
import com.awbd.awbd.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private ServiceTypeService serviceTypeService;

    @MockitoBean
    private ReceiptService receiptService;

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testAppointmentForm() throws Exception {
        Long mechanicId = 1L;
        VehicleDto vehicle = new VehicleDto();
        ServiceTypeDto serviceType = new ServiceTypeDto();

        when(vehicleService.findClientVehicles()).thenReturn(List.of(vehicle));
        when(serviceTypeService.findMechanicServiceTypes(mechanicId)).thenReturn(List.of(serviceType));

        mockMvc.perform(get("/appointment/mechanic/{mechanicId}", mechanicId))
                .andExpect(status().isOk())
                .andExpect(view().name("appointmentForm"))
                .andExpect(model().attributeExists("appointment"))
                .andExpect(model().attributeExists("vehicles"))
                .andExpect(model().attributeExists("serviceTypes"));

        verify(vehicleService).findClientVehicles();
        verify(serviceTypeService).findMechanicServiceTypes(mechanicId);
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testSaveOrUpdate_Valid() throws Exception {
        doNothing().when(appointmentService).save(ArgumentMatchers.any(AppointmentDto.class));

        mockMvc.perform(post("/appointment")
                        .param("mechanicId", "1")
                        .param("vehicleId", "2")
                        .param("dateTime", LocalDateTime.now().plusDays(1).toString())
                        .param("serviceTypeIds[0]", "3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointment"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testSaveOrUpdate_WithValidationErrors() throws Exception {
        VehicleDto vehicle = new VehicleDto();
        ServiceTypeDto serviceType = new ServiceTypeDto();

        when(vehicleService.findClientVehicles()).thenReturn(List.of(vehicle));
        when(serviceTypeService.findMechanicServiceTypes(1L)).thenReturn(List.of(serviceType));

        mockMvc.perform(post("/appointment")
                        .param("mechanicId", "1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("appointmentForm"))
                .andExpect(model().attributeExists("vehicles"))
                .andExpect(model().attributeExists("serviceTypes"))
                .andExpect(model().attributeExists("appointment"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testAppointmentList() throws Exception {
        when(appointmentService.findAppointments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/appointment"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointmentList"))
                .andExpect(model().attributeExists("appointments"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "MECHANIC")
    void testGenerateReceipt() throws Exception {
        doNothing().when(receiptService).generateReceiptForAppointment(1L);

        mockMvc.perform(post("/appointment/{appointmentId}/receipt", 1L).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointment"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testViewReceipt() throws Exception {
        ReceiptDto receipt = new ReceiptDto();
        when(receiptService.getReceiptByAppointmentId(1L)).thenReturn(receipt);

        mockMvc.perform(get("/appointment/receipt/{appointmentId}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name("receiptView"))
                .andExpect(model().attributeExists("receipt"));
    }
}
