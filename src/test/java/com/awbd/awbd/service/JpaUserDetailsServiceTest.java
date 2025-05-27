package com.awbd.awbd.service;

import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Role;
import com.awbd.awbd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaUserDetailsServiceTest {

    @InjectMocks
    private JpaUserDetailsService jpaUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private Client mockClient;

    @BeforeEach
    void setUp() {
        mockClient = new Client();
        mockClient.setUsername("john");
        mockClient.setPassword("encodedPassword");
        mockClient.setEnabled(true);
        mockClient.setAccountNonExpired(true);
        mockClient.setAccountNonLocked(true);
        mockClient.setCredentialsNonExpired(true);
        mockClient.setRole(Role.CLIENT);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        when(userRepository.findByUsername("john")).thenReturn(mockClient);

        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT")));

        verify(userRepository, times(1)).findByUsername("john");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("john")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> jpaUserDetailsService.loadUserByUsername("john"));

        verify(userRepository, times(1)).findByUsername("john");
    }

    @Test
    void getAuthorities_ShouldReturnEmptyCollection_WhenRoleIsNull() {
        mockClient.setRole(null);
        when(userRepository.findByUsername("john")).thenReturn(mockClient);

        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().isEmpty());
    }
}
