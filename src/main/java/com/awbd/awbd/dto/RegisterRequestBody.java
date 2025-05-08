package com.awbd.awbd.dto;

import lombok.Data;

@Data
public class RegisterRequestBody {
    private String username;
    private String password;
    private String role;
}
