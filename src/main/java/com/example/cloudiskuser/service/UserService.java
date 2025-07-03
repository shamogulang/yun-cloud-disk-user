package com.example.cloudiskuser.service;

import com.example.cloudiskuser.model.LoginResp;
import com.example.cloudiskuser.model.User;

public interface UserService {
    User register(User user, String code);
    LoginResp login(String identifier, String passwordOrCode);
    void logout(String token);
} 