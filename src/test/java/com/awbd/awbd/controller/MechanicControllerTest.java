package com.awbd.awbd.controller;

import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Role;
import com.awbd.awbd.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MechanicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        Client client = Client.builder()
                .username("client")
                .password("12345")
                .role(Role.CLIENT)
                .build();
        userRepository.save(client);
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testMechanicList() throws Exception {
        mockMvc.perform(get("/mechanic"))
                .andExpect(status().isOk())
                .andExpect(view().name("mechanicList"))
                .andExpect(model().attributeExists("mechanics"));
    }
}
