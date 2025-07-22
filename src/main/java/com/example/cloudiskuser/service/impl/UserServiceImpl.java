package com.example.cloudiskuser.service.impl;

import com.example.cloudiskuser.feign.DiskFeign;
import com.example.cloudiskuser.feign.SpaceFeign;
import com.example.cloudiskuser.mapper.UserMapper;
import com.example.cloudiskuser.model.*;
import com.example.cloudiskuser.service.UserService;
import com.example.cloudiskuser.util.JwtUtil;
import com.example.cloudiskuser.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private DiskFeign baseFeign;
    @Autowired
    private SpaceFeign spaceFeign;

    @Override
    public User register(User user, String code) {
        // 校验唯一性
        if (StringUtils.hasText(user.getUsername()) && userMapper.selectByUsername(user.getUsername()) != null)
            throw new RuntimeException("用户名已存在");
        if (StringUtils.hasText(user.getEmail()) && userMapper.selectByEmail(user.getEmail()) != null)
            throw new RuntimeException("邮箱已存在");
        if (StringUtils.hasText(user.getPhone()) && userMapper.selectByPhone(user.getPhone()) != null)
            throw new RuntimeException("手机号已存在");

        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        userMapper.insertUser(user);
        // 调用底座初始化
        baseFeign.initUser(user.getId());
        // 分配20G空间
        spaceFeign.createSpace(user.getId(), 20 * 1024L * 1024 * 1024);
        return user;
    }

    @Override
    public LoginResp login(String identifier, String passwordOrCode) {
        User user = userMapper.selectByUsername(identifier);
        if (user == null) user = userMapper.selectByEmail(identifier);
        if (user == null) user = userMapper.selectByPhone(identifier);
        if (user == null) throw new RuntimeException("用户不存在");
        // TODO: 支持验证码登录
        if (!PasswordUtil.matches(passwordOrCode, user.getPassword())) throw new RuntimeException("密码错误");
        String token = jwtUtil.generateToken(user);
        return new LoginResp(token, user.getUsername(), user.getId());
    }

    @Override
    public void logout(String token) {
        // JWT无状态，前端删除token即可
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = userMapper.selectById(userId);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        return userDto;
    }

    @Override
    public AuthResponse auth(AuthRequest request) {
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
            return resp;
        } catch (Exception e) {
            resp.setValid(false);
            resp.setMessage("token无效或已过期");
            return resp;
        }
    }
}