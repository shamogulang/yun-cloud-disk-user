package com.example.cloudiskuser.service;

import com.example.cloudiskuser.model.*;

public interface UserService {
    User register(User user, String code);
    LoginResp login(String identifier, String passwordOrCode);
    void logout(String token);

    UserDto getUser(Long userId);

    AuthResponse auth(AuthRequest request);
} 