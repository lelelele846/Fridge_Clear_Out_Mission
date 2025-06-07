package com.example.backend.controller;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.LoginInfo;
import com.example.backend.pojo.Result;
import com.example.backend.pojo.User;
import com.example.backend.service.LoginService;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
       @PostMapping("/login")
    public Result login(@RequestBody User user) {

           log.info("Login User: {}", user);
           LoginInfo loginInfo = loginService.login(user);
           if (loginInfo != null) {
               return Result.success(loginInfo);
           }
           return Result.error();
       }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
           log.info("Register User: {}", user);
        List<User> users = userService.register(user);
        if(!users.isEmpty()){
            return Result.error();
        }
        userService.add(user);
        return Result.success();
    }

}
