package com.truecodes.UserServiceApplication.service;

import com.truecodes.UserServiceApplication.model.Users;
import com.truecodes.UserServiceApplication.repository.UserRepository;
import com.truecodes.utilities.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    public String generateToken(String username) {
//        return jwtUtil.generateToken(username);
//    }

    public boolean validateUser(String username, String password){
        Optional<Users> optionalUser = userRepository.findByEmail(username);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("User Not Found");
        }
        Users user = optionalUser.get();
        return bCryptPasswordEncoder.matches(password, user.getPassword());

    }

    public String generateLoginToken(String username, String password, String checkToken, int expiresIn, String type) {
        return jwtUtil.generateToken(username, password, checkToken ,expiresIn, type);
    }

}
