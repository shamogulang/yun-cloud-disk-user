package com.example.cloudiskuser.service;

import com.example.cloudiskuser.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testRegisterAndLogin() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhone("13800000000");
        user.setPassword("test1234");
        User saved = userService.register(user, null);
        Assertions.assertNotNull(saved.getId());
        String token = userService.login("testuser", "test1234");
        Assertions.assertNotNull(token);
    }
} 