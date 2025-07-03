package com.example.cloudiskuser.mapper;

import com.example.cloudiskuser.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectByUsername(@Param("username") String username);
    User selectByEmail(@Param("email") String email);
    User selectByPhone(@Param("phone") String phone);
    int insertUser(User user);
} 