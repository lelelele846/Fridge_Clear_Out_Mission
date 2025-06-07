package com.example.backend.service;

import com.example.backend.pojo.User;

import java.util.List;

public interface UserService {
    void add(User user);
    List<User> register(User user);
}
