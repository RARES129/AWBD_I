package com.awbd.awbd.mapper;

import com.awbd.awbd.dto.UserDto;
import com.awbd.awbd.entity.Client;
import com.awbd.awbd.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl();
    }

    @Test
    void updateUserFromRequest_withValidDto_updatesClientFields() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("johndoe");
        userDto.setPassword("securepassword");
        userDto.setRole(Role.CLIENT);

        Client client = Client.builder()
                .id(99L)
                .username("olduser")
                .password("oldpass")
                .role(Role.MECHANIC)
                .build();

        // Act
        mapper.updateUserFromRequest(userDto, client);

        // Assert
        assertEquals(userDto.getId(), client.getId());
        assertEquals(userDto.getUsername(), client.getUsername());
        assertEquals(userDto.getPassword(), client.getPassword());
        assertEquals(userDto.getRole(), client.getRole());
    }

    @Test
    void updateUserFromRequest_withNullDto_doesNotThrowAndLeavesClientUnchanged() {
        // Arrange
        Client client = Client.builder()
                .id(5L)
                .username("clientuser")
                .password("clientpass")
                .role(Role.CLIENT)
                .build();

        // Capture initial values
        Long initialId = client.getId();
        String initialUsername = client.getUsername();
        String initialPassword = client.getPassword();
        Role initialRole = client.getRole();

        // Act
        mapper.updateUserFromRequest(null, client);

        // Assert
        assertEquals(initialId, client.getId());
        assertEquals(initialUsername, client.getUsername());
        assertEquals(initialPassword, client.getPassword());
        assertEquals(initialRole, client.getRole());
    }
}
