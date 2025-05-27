package com.awbd.awbd.service;

import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.mapper.MechanicMapper;
import com.awbd.awbd.repository.MechanicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MechanicServiceTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @Mock
    private MechanicMapper mechanicMapper;

    @InjectMocks
    private MechanicService mechanicService;

    @Test
    void findAll_ShouldReturnListOfMechanicDtos() {
        // Arrange
        Mechanic mechanic1 = new Mechanic(); mechanic1.setId(1L);
        Mechanic mechanic2 = new Mechanic(); mechanic2.setId(2L);

        MechanicDto dto1 = new MechanicDto(); dto1.setId(1L);
        MechanicDto dto2 = new MechanicDto(); dto2.setId(2L);

        when(mechanicRepository.findAll()).thenReturn(List.of(mechanic1, mechanic2));
        when(mechanicMapper.toMechanicDto(mechanic1)).thenReturn(dto1);
        when(mechanicMapper.toMechanicDto(mechanic2)).thenReturn(dto2);

        // Act
        List<MechanicDto> result = mechanicService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));

        verify(mechanicMapper, times(1)).toMechanicDto(mechanic1);
        verify(mechanicMapper, times(1)).toMechanicDto(mechanic2);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoMechanicsFound() {
        when(mechanicRepository.findAll()).thenReturn(List.of());

        List<MechanicDto> result = mechanicService.findAll();

        assertTrue(result.isEmpty());
        verify(mechanicRepository, times(1)).findAll();
        verify(mechanicMapper, never()).toMechanicDto(any());
    }
}
