package com.awbd.awbd.controller;

import com.awbd.awbd.dto.VehicleDto;
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

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleService vehicleService;

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testVehicleForm() throws Exception {
        mockMvc.perform(get("/vehicle/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicleForm"))
                .andExpect(model().attributeExists("vehicle"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testSaveOrUpdate_Valid() throws Exception {
        doNothing().when(vehicleService).save(ArgumentMatchers.any(VehicleDto.class));

        mockMvc.perform(post("/vehicle")
                        .param("brand", "Toyota")
                        .param("model", "Corolla")
                        .param("plateNumber", "AB123CD")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vehicle"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testSaveOrUpdate_Invalid() throws Exception {
        mockMvc.perform(post("/vehicle")
                        .param("brand", "")
                        .param("model", "")
                        .param("plateNumber", "invalid!")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicleForm"))
                .andExpect(model().attributeHasFieldErrors("vehicle", "brand", "model", "plateNumber"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testVehicleList() throws Exception {
        when(vehicleService.findClientVehicles()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/vehicle"))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicleList"))
                .andExpect(model().attributeExists("vehicles"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testEdit() throws Exception {
        Long vehicleId = 1L;
        VehicleDto vehicle = new VehicleDto();
        when(vehicleService.findById(vehicleId)).thenReturn(vehicle);
        doNothing().when(vehicleService).ensureNotInUse(vehicleId);

        mockMvc.perform(get("/vehicle/edit/{id}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicleForm"))
                .andExpect(model().attributeExists("vehicle"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testDeleteById() throws Exception {
        Long vehicleId = 1L;
        doNothing().when(vehicleService).deleteById(vehicleId);

        mockMvc.perform(get("/vehicle/delete/{id}", vehicleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vehicle"));
    }
}
