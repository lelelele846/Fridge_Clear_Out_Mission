package com.example.backend.service.impl;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.LoginInfo;
import com.example.backend.pojo.User;
import com.example.backend.service.LoginService;
import com.example.backend.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public LoginInfo login(User user) {
        User loginUser = userMapper.getByUsername(user);
        if(loginUser!=null&&BCrypt.checkpw(user.getPassword(),loginUser.getPassword())){
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsername(loginUser.getUsername());
            loginInfo.setEmail(loginUser.getEmail());
            loginInfo.setId(loginUser.getId());
            Map<String,Object> claims = new HashMap<>();
            claims.put("username",loginUser.getUsername());
            claims.put("id",loginUser.getId());
            String token = JwtUtils.generateToken(claims);
            loginInfo.setToken(token);
            return loginInfo;
        }
        return null;
    }

}
