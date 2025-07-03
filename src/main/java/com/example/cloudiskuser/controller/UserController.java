package com.example.cloudiskuser.controller;

import com.example.cloudiskuser.model.AuthRequest;
import com.example.cloudiskuser.model.AuthResponse;
import com.example.cloudiskuser.model.LoginRequest;
import com.example.cloudiskuser.model.LoginResp;
import com.example.cloudiskuser.model.Result;
import com.example.cloudiskuser.model.User;
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

    @PostMapping("/logout")
    public Result<LogoutResp> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token);
        return Result.success(new LogoutResp("登出成功"));
    }

    @PostMapping("/auth")
    public Result<AuthResponse> auth(@RequestBody AuthRequest request) {
        AuthResponse resp = new AuthResponse();
        try {
            String token = request.getToken();
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtil.getClaimsFromToken(token);
            resp.setValid(true);
            resp.setUserId(Long.valueOf(claims.getSubject()));
            resp.setUsername((String) claims.get("username"));
            resp.setMessage("token有效");
            return Result.success(resp);
        } catch (Exception e) {
            resp.setValid(false);
            resp.setMessage("token无效或已过期");
            return Result.success(resp);
        }
    }
} 