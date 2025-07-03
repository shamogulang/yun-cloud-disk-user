package com.example.cloudiskuser.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;

    private String username;

    private String email;

    private String phone;

    private String password;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 