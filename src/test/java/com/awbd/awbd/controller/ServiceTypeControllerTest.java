package com.awbd.awbd.controller;

import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.service.ServiceTypeService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
class ServiceTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceTypeService serviceTypeService;

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "MECHANIC")
    void testServiceTypeForm() throws Exception {
        mockMvc.perform(get("/service-type/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("serviceTypeForm"))
                .andExpect(model().attributeExists("serviceType"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "MECHANIC")
    void testSaveOrUpdate_Valid() throws Exception {
        doNothing().when(serviceTypeService).save(ArgumentMatchers.any(ServiceTypeDto.class));

        mockMvc.perform(post("/service-type")
                        .param("name", "Oil Change")
                        .param("price", "20.0")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/service-type"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "MECHANIC")
    void testSaveOrUpdate_WithValidationErrors() throws Exception {
        mockMvc.perform(post("/service-type")
                        .param("name", "")
                        .param("price", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("serviceTypeForm"))
                .andExpect(model().attributeExists("serviceType"));

        verify(serviceTypeService, never()).save(any());
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "MECHANIC")
    void testServiceTypeList() throws Exception {
        Page<ServiceTypeDto> serviceTypePage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 5), 0);
        when(serviceTypeService.findMechanicServiceTypesPaginated(PageRequest.of(0, 5, Sort.by("id"))))
                .thenReturn(serviceTypePage);

        mockMvc.perform(get("/service-type"))
                .andExpect(status().isOk())
                .andExpect(view().name("serviceTypeList"))
                .andExpect(model().attributeExists("serviceTypePage"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("sortBy"));
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "MECHANIC")
    void testEdit() throws Exception {
        ServiceTypeDto dto = new ServiceTypeDto();
        dto.setId(1L);
        dto.setName("Oil Change");
        dto.setPrice(20.0);

        when(serviceTypeService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/service-type/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("serviceTypeForm"))
                .andExpect(model().attributeExists("serviceType"));

        verify(serviceTypeService, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "MECHANIC")
    void testDeleteById() throws Exception {
        doNothing().when(serviceTypeService).deleteById(1L);

        mockMvc.perform(get("/service-type/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/service-type"));

        verify(serviceTypeService, times(1)).deleteById(1L);
    }
}
