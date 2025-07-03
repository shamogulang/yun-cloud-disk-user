package com.example.cloudiskuser.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResp {

    public String token;
    private String username;
    private Long userId;
}
