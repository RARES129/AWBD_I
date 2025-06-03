package com.awbd.awbd.controller;

import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.Role;
import com.awbd.awbd.entity.ServiceType;
import com.awbd.awbd.repository.ServiceTypeRepository;
import com.awbd.awbd.repository.UserRepository;
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
class ServiceTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    private Long serviceTypeId;

    @BeforeEach
    void setUp() {
        Mechanic mechanic = Mechanic.builder()
                .username("mechanic")
                .password("12345")
                .role(Role.MECHANIC)
                .build();
        mechanic = userRepository.save(mechanic);

        ServiceType serviceType = ServiceType.builder()
                .name("Oil Change")
                .price(20.0)
                .mechanic(mechanic)
                .build();
        serviceType = serviceTypeRepository.save(serviceType);
        serviceTypeId = serviceType.getId();
    }

    @Test
    @WithUserDetails(value = "mechanic", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testServiceTypeForm() throws Exception {
        mockMvc.perform(get("/service-type/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("serviceTypeForm"))
                .andExpect(model().attributeExists("serviceType"));
    }

    @Test
    @WithUserDetails(value = "mechanic", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testSaveOrUpdate_Valid() throws Exception {
        mockMvc.perform(post("/service-type")
                        .param("name", "Tire Rotation")
                        .param("price", "30.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/service-type"));
    }

    @Test
    @WithUserDetails(value = "mechanic", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testSaveOrUpdate_WithValidationErrors() throws Exception {
        mockMvc.perform(post("/service-type")
                        .param("name", "")
                        .param("price", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("serviceTypeForm"))
                .andExpect(model().attributeExists("serviceType"));
    }

    @Test
    @WithUserDetails(value = "mechanic", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testServiceTypeList() throws Exception {
        mockMvc.perform(get("/service-type"))
                .andExpect(status().isOk())
                .andExpect(view().name("serviceTypeList"))
                .andExpect(model().attributeExists("serviceTypePage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("sortBy"));
    }

    @Test
    @WithUserDetails(value = "mechanic", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testEdit() throws Exception {
        mockMvc.perform(get("/service-type/edit/{id}", serviceTypeId))
                .andExpect(status().isOk())
                .andExpect(view().name("serviceTypeForm"))
                .andExpect(model().attributeExists("serviceType"));
    }

    @Test
    @WithUserDetails(value = "mechanic", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testDeleteById() throws Exception {
        mockMvc.perform(get("/service-type/delete/{id}", serviceTypeId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/service-type"));
    }
}