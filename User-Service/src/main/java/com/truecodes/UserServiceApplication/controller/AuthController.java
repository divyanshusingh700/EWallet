package com.truecodes.UserServiceApplication.controller;

import com.truecodes.UserServiceApplication.dtos.*;
import com.truecodes.UserServiceApplication.model.OtpEntry;
import com.truecodes.UserServiceApplication.service.AuthService;
import com.truecodes.UserServiceApplication.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RegistrationService registrationService;
//    @GetMapping("/get-token")
//    public ResponseEntity<String> generateToken(@RequestParam String username) {
//        return ResponseEntity.ok(authService.generateToken(username));
//    }
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public String createToken(String username, String password, String checkToken, int expiresIn, String type) {
        return authService.generateLoginToken(username, password, checkToken, expiresIn, type);
    }

    @PostMapping
    public ResponseEntity<AuthResponse> authenticate(@RequestBody @Valid AuthRequest request, HttpServletRequest servletRequest) {
        kafkaTemplate.send("login-data", null, null, request.getUsername()+request.getOtpCheckToken()+request.getRefreshOtpCheckToken());
        int expiresIn = 10 * 60 * 1000;
        String accessToken = this.createToken(request.getUsername(), request.getPassword(), request.getOtpCheckToken(), expiresIn, "access");
        String refreshToken = this.createToken(request.getUsername(), request.getPassword(), request.getRefreshOtpCheckToken(), expiresIn, "refresh");
        return ResponseEntity.ok(new AuthResponse(expiresIn, "BEARER", accessToken, refreshToken));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, String>> authRequestOtp(@RequestBody @Valid AuthOtpRequestDTO request) {
        kafkaTemplate.send("otp-login", request.getLogin());
        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("status", "ok");
        responseJson.put("message", "OTP generated successfully");
        responseJson.put("validationMethod", "Email");
        return ResponseEntity.ok(responseJson);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> verifyLogin(@RequestBody @Valid AuthOtpConfirmDTO request) {
        boolean isValid = registrationService.verifyOtp(request.getLogin(), request.getOtp(), OtpEntry.ActionType.LOGIN);
        if (isValid) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OTPResponse("Invalid or expired OTP"));
        }
    }
}
