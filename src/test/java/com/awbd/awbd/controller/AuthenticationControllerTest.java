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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthenticationControllerTest {

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
    void testShowRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testRegister_WithValidInput_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "newuser")
                        .param("password", "strongPass123")
                        .param("role", "CLIENT")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testRegister_WithValidationErrors_ShouldReturnToForm() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "")
                        .param("password", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithUserDetails(value = "client", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void testAccessDeniedPage() throws Exception {
        mockMvc.perform(get("/access_denied"))
                .andExpect(status().isOk())
                .andExpect(view().name("accessDenied"));
    }
}
