package com.example.cloudiskuser.model;

import lombok.Data;

@Data
public class AuthResponse {
    private boolean valid;
    private Long userId;
    private String username;
    private String message;
} 