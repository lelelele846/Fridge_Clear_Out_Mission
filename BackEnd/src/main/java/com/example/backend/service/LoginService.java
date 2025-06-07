package com.example.backend.service;

import com.example.backend.pojo.LoginInfo;
import com.example.backend.pojo.User;

import java.util.List;

public interface LoginService {
    LoginInfo login(User user);

}
