package com.truecodes.UserServiceApplication.controller;

import com.truecodes.UserServiceApplication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<String> generateToken(@RequestParam String username) {
        return ResponseEntity.ok(authService.generateToken(username));
    }
}
