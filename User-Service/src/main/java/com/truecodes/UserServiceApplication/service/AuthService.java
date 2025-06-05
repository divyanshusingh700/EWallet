package com.truecodes.UserServiceApplication.service;

import com.truecodes.utilities.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JwtUtil jwtUtil;

    public String generateToken(String username) {
        return jwtUtil.generateToken(username);
    }
}
