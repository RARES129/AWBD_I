package com.awbd.awbd.service;

import com.awbd.awbd.dto.ServiceTypeDto;
import com.awbd.awbd.entity.Appointment;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.Receipt;
import com.awbd.awbd.entity.ServiceType;
import com.awbd.awbd.exceptions.EntityInUnfinishedAppointmentException;
import com.awbd.awbd.exceptions.ResourceNotFoundException;
import com.awbd.awbd.mapper.ServiceTypeMapper;
import com.awbd.awbd.repository.AppointmentRepository;
import com.awbd.awbd.repository.MechanicRepository;
import com.awbd.awbd.repository.ServiceTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceTypeServiceTest {

    @InjectMocks
    private ServiceTypeService service;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @Mock
    private ServiceTypeMapper serviceTypeMapper;

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    void save_shouldSaveServiceType() {
        ServiceTypeDto dto = new ServiceTypeDto();
        Mechanic mechanic = new Mechanic();
        ServiceType serviceType = new ServiceType();

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getSessionUsername).thenReturn("mech1");
            when(mechanicRepository.findByUsername("mech1")).thenReturn(mechanic);
            when(serviceTypeMapper.toServiceType(dto, mechanic)).thenReturn(serviceType);

            service.save(dto);

            verify(serviceTypeRepository).save(serviceType);
        }
    }

    @Test
    void findMechanicServiceTypes_shouldReturnDtosForLoggedInMechanic() {
        Mechanic mechanic = new Mechanic();
        mechanic.setId(1L);
        ServiceType st = new ServiceType();
        ServiceTypeDto dto = new ServiceTypeDto();

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getSessionUsername).thenReturn("mech1");
            when(mechanicRepository.findByUsername("mech1")).thenReturn(mechanic);
            when(serviceTypeRepository.findByMechanicId(1L)).thenReturn(List.of(st));
            when(serviceTypeMapper.toServiceTypeDto(st)).thenReturn(dto);

            List<ServiceTypeDto> result = service.findMechanicServiceTypes();

            assertEquals(1, result.size());
            verify(serviceTypeMapper).toServiceTypeDto(st);
        }
    }

    @Test
    void findMechanicServiceTypesPaginated_shouldReturnPagedServiceTypeDtos() {
        Mechanic mechanic = new Mechanic();
        mechanic.setId(1L);
        ServiceType serviceType = new ServiceType();
        ServiceTypeDto dto = new ServiceTypeDto();
        Pageable pageable = PageRequest.of(0, 5);
        Page<ServiceType> serviceTypePage = new PageImpl<>(List.of(serviceType));

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getSessionUsername).thenReturn("mechanic1");

            when(mechanicRepository.findByUsername("mechanic1")).thenReturn(mechanic);
            when(serviceTypeRepository.findByMechanicId(1L, pageable)).thenReturn(serviceTypePage);
            when(serviceTypeMapper.toServiceTypeDto(serviceType)).thenReturn(dto);

            Page<ServiceTypeDto> result = service.findMechanicServiceTypesPaginated(pageable);

            assertEquals(1, result.getTotalElements());
            assertEquals(dto, result.getContent().getFirst());
        }
    }


    @Test
    void findMechanicServiceTypes_withMechanicId_shouldReturnDtos() {
        ServiceType st = new ServiceType();
        ServiceTypeDto dto = new ServiceTypeDto();

        when(serviceTypeRepository.findByMechanicId(2L)).thenReturn(List.of(st));
        when(serviceTypeMapper.toServiceTypeDto(st)).thenReturn(dto);

        List<ServiceTypeDto> result = service.findMechanicServiceTypes(2L);

        assertEquals(1, result.size());
    }

    @Test
    void deleteById_shouldDeleteIfNoUnfinishedAppointments() {
        Appointment finishedAppointment = new Appointment();
        finishedAppointment.setServiceTypes(new ArrayList<>());
        finishedAppointment.setReceipt(new Receipt());

        ServiceType toRemove = new ServiceType();
        toRemove.setId(1L);
        finishedAppointment.getServiceTypes().add(toRemove);

        when(appointmentRepository.findByServiceTypes_Id(1L)).thenReturn(List.of(finishedAppointment));

        service.deleteById(1L);

        verify(appointmentRepository).saveAll(anyList());
        verify(serviceTypeRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrowExceptionIfUnfinishedAppointment() {
        Appointment unfinishedAppointment = new Appointment();
        unfinishedAppointment.setServiceTypes(List.of(new ServiceType(1L, "Test", 100.0, null)));

        when(appointmentRepository.findByServiceTypes_Id(1L)).thenReturn(List.of(unfinishedAppointment));

        EntityInUnfinishedAppointmentException ex = assertThrows(EntityInUnfinishedAppointmentException.class, () -> service.deleteById(1L));
        assertEquals("Service Type is used in an unfinished appointment.", ex.getMessage());
    }

    @Test
    void findById_shouldReturnDto() {
        ServiceType st = new ServiceType();
        ServiceTypeDto dto = new ServiceTypeDto();

        when(serviceTypeRepository.findById(5L)).thenReturn(Optional.of(st));
        when(serviceTypeMapper.toServiceTypeDto(st)).thenReturn(dto);

        ServiceTypeDto result = service.findById(5L);

        assertSame(dto, result);
    }

    @Test
    void findById_shouldThrowExceptionIfNotFound() {
        when(serviceTypeRepository.findById(5L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.findById(5L));
        assertEquals("Service Type not found with id: 5", ex.getMessage());
    }
}
