package com.awbd.awbd.dto;

import lombok.Data;

@Data
public class UserCreationRequestDto {
    private String username;
    private String password;
    private String role;
}
