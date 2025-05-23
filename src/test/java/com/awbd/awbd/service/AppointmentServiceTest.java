package com.awbd.awbd.service;

import com.awbd.awbd.config.SecurityUtil;
import com.awbd.awbd.dto.AppointmentDto;
import com.awbd.awbd.entity.*;
import com.awbd.awbd.mapper.AppointmentMapper;
import com.awbd.awbd.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    private AppointmentDto appointmentDto;
    private Client client;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentDto = new AppointmentDto(
                1L,
                LocalDateTime.now().plusDays(1),
                null,
                2L,
                3L,
                List.of(4L)
        );

        client = Client.builder()
                .id(1L)
                .username("testClient")
                .build();

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setClient(client);
    }

    @Test
    void save_ShouldSaveAppointmentSuccessfully() {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getSessionUsername).thenReturn("testClient");
            when(clientRepository.findByUsername("testClient")).thenReturn(client);
            when(appointmentMapper.toAppointment(eq(appointmentDto), eq(client), any(), any(), any()))
                    .thenReturn(appointment);

            appointmentService.save(appointmentDto);

            verify(appointmentRepository).save(appointment);
        }
    }

    @Test
    void findAppointments_ShouldReturnClientAppointments() {
        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getSessionUsername).thenReturn("testClient");
            when(userRepository.findByUsername("testClient")).thenReturn(client);

            List<Appointment> expected = List.of(appointment);
            when(appointmentRepository.findByClientIdOrderByDateTimeAsc(1L)).thenReturn(expected);

            List<Appointment> actual = appointmentService.findAppointments();

            assertEquals(expected, actual);
        }
    }

    @Test
    void findAppointments_ShouldReturnMechanicAppointments() {
        Mechanic mechanic = Mechanic.builder().id(2L).username("testMechanic").build();

        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getSessionUsername).thenReturn("testMechanic");
            when(userRepository.findByUsername("testMechanic")).thenReturn(mechanic);

            List<Appointment> expected = List.of(appointment);
            when(appointmentRepository.findByMechanicIdOrderByDateTimeAsc(2L)).thenReturn(expected);

            List<Appointment> actual = appointmentService.findAppointments();

            assertEquals(expected, actual);
        }
    }

    @Test
    void findAppointments_ShouldReturnEmptyList_ForInvalidUser() {
        User genericUser = new User() {
        };
        genericUser.setId(99L);
        genericUser.setUsername("genericUser");

        try (MockedStatic<SecurityUtil> mockedSecurityUtil = mockStatic(SecurityUtil.class)) {
            mockedSecurityUtil.when(SecurityUtil::getSessionUsername).thenReturn("genericUser");
            when(userRepository.findByUsername("genericUser")).thenReturn(genericUser);

            List<Appointment> result = appointmentService.findAppointments();
            assertTrue(result.isEmpty());
        }
    }
}
