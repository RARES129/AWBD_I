package com.awbd.awbd.service;

import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.Role;
import com.awbd.awbd.entity.User;
import com.awbd.awbd.mapper.UserMapper;
import com.awbd.awbd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("plainpass");
    }

    @Test
    void register_ShouldSaveClient() {
        userDto.setRole(Role.CLIENT);

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(passwordEncoder.encode("plainpass")).thenReturn("encodedpass");

        authenticationService.register(userDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).updateUserFromRequest(eq(userDto), userCaptor.capture());
        verify(userRepository).save(userCaptor.getValue());

        assertTrue(userCaptor.getValue() instanceof Client);
        assertEquals("encodedpass", userDto.getPassword());
    }

    @Test
    void register_ShouldSaveMechanic() {
        userDto.setRole(Role.MECHANIC);

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(passwordEncoder.encode("plainpass")).thenReturn("encodedpass");

        authenticationService.register(userDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).updateUserFromRequest(eq(userDto), userCaptor.capture());
        verify(userRepository).save(userCaptor.getValue());

        assertTrue(userCaptor.getValue() instanceof Mechanic);
        assertEquals("encodedpass", userDto.getPassword());
    }

    @Test
    void register_ShouldThrowExceptionForExistingUsername() {
        userDto.setRole(Role.CLIENT);

        when(userRepository.findByUsername("testuser")).thenReturn(mock(User.class));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                authenticationService.register(userDto));

        assertEquals("Username is already taken", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_ShouldThrowExceptionForAdminRole() {
        userDto.setRole(Role.ADMIN);

        when(userRepository.findByUsername("testuser")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                authenticationService.register(userDto));

        assertEquals("Invalid role", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
