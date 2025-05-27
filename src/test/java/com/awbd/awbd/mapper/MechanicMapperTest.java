package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.MechanicDto;
import com.awbd.awbd.entity.Mechanic;
import com.awbd.awbd.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class MechanicMapperTest {

    private MechanicMapper mechanicMapper;

    @BeforeEach
    void setUp() {
        mechanicMapper = Mappers.getMapper(MechanicMapper.class);
    }

    @Test
    void testToMechanicDto_successfulMapping() {
        // Arrange
        Mechanic mechanic = new Mechanic();
        mechanic.setId(1L);
        mechanic.setUsername("mechanic");
        mechanic.setPassword("secret");
        mechanic.setRole(Role.MECHANIC);

        // Act
        MechanicDto dto = mechanicMapper.toMechanicDto(mechanic);

        // Assert
        assertNotNull(dto);
        assertEquals(mechanic.getId(), dto.getId());
        assertEquals(mechanic.getUsername(), dto.getUsername());
        assertEquals(mechanic.getPassword(), dto.getPassword());
        assertEquals(mechanic.getRole(), dto.getRole());
    }

    @Test
    void testToMechanicDto_nullMechanic_returnsNull() {
        // Act
        MechanicDto dto = mechanicMapper.toMechanicDto(null);

        // Assert
        assertNull(dto);
    }
}
