package com.example.backend.service.impl;

import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import com.example.backend.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

 // 注入 PasswordEncoder
    @Autowired
    private UserMapper userMapper;
    @Override
    public void add(User user) {
        user.setCreateTime(LocalDateTime.now());
        // 密码加密
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userMapper.add(user);
    }

    @Override
    public List<User> register(User user) {
        List<User> users = userMapper.findUserByEmailAndUsername();
        return users;
    }
}
