package com.awbd.awbd.controller;

import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Role;
import com.awbd.awbd.entity.Vehicle;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private Long vehicleId;

    @BeforeEach
    void setUp() {
        Client client = Client.builder()
                .username("client")
                .password("12345")
                .role(Role.CLIENT)
                .build();
        client = userRepository.save(client);

        Vehicle vehicle = Vehicle.builder()
                .brand("Toyota")
                .model("Corolla")
                .plateNumber("AB123CD")
                .owner(client)
                .build();
        vehicle = vehicleRepository.save(vehicle);
        vehicleId = vehicle.getId();
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testVehicleForm() throws Exception {
        mockMvc.perform(get("/vehicle/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicleForm"))
                .andExpect(model().attributeExists("vehicle"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testSaveOrUpdate_Valid() throws Exception {
        mockMvc.perform(post("/vehicle")
                        .param("brand", "Toyota")
                        .param("model", "Corolla")
                        .param("plateNumber", "AB123DE")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vehicle"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
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
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testSaveOrUpdate_DuplicatePlateNumber() throws Exception {
        mockMvc.perform(post("/vehicle")
                        .param("brand", "Toyota")
                        .param("model", "Corolla")
                        .param("plateNumber", "AB123CD")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicleForm"))
                .andExpect(model().attributeHasFieldErrors("vehicle", "plateNumber"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testVehicleList() throws Exception {
        mockMvc.perform(get("/vehicle"))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicleList"))
                .andExpect(model().attributeExists("vehiclePage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("sortBy"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEdit() throws Exception {
        mockMvc.perform(get("/vehicle/edit/{id}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(view().name("vehicleForm"))
                .andExpect(model().attributeExists("vehicle"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeleteById() throws Exception {
        mockMvc.perform(get("/vehicle/delete/{id}", vehicleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vehicle"));
    }
}
