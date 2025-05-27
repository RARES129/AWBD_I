package com.awbd.awbd.controller;

import com.awbd.awbd.service.MechanicService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MechanicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MechanicService mechanicService;

    @Test
    @WithMockUser(username = "testUser", password = "12345", roles = "CLIENT")
    void testMechanicList() throws Exception {
        when(mechanicService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/mechanic"))
                .andExpect(status().isOk())
                .andExpect(view().name("mechanicList"))
                .andExpect(model().attributeExists("mechanics"));
    }
}
