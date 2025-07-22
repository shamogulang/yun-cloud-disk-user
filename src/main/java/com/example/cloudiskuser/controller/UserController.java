package com.example.cloudiskuser.controller;

import com.example.cloudiskuser.model.*;
import com.example.cloudiskuser.service.UserService;
import com.example.cloudiskuser.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user, @RequestParam(required = false) String code) {
        try {
            User saved = userService.register(user, code);
            return Result.success(saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<LoginResp> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResp loginResp = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return Result.success(loginResp);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Data
    static class LogoutResp {
        public String message;
        public LogoutResp(String message) { this.message = message; }
    }

    @GetMapping("/logout")
    public Result<LogoutResp> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return Result.success(new LogoutResp("登出成功"));
    }

    @GetMapping("/{userId}")
    public Result<UserDto> getUserInfo(@PathVariable("userId") Long userId) {

        return Result.success(userService.getUser(userId));
    }


    @PostMapping("/auth")
    public Result<AuthResponse> auth(@RequestBody AuthRequest request) {
          return Result.success(userService.auth(request));
    }
} 