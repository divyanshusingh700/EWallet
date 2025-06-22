package com.truecodes.UserServiceApplication.controller;

import com.truecodes.UserServiceApplication.dtos.*;
import com.truecodes.UserServiceApplication.model.OtpEntry;
import com.truecodes.UserServiceApplication.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/initiate")
    public ResponseEntity<OTPResponse> initiateRegistration(@RequestBody @Valid RegistrationInitiateRequest request, HttpServletRequest servletRequest) {
        kafkaTemplate.send("otp-registration", request.getUsername());
        return ResponseEntity.ok(new OTPResponse("Request OTP by EMAIL initiated!"));
    }

    @PostMapping("/verify")
    public ResponseEntity<OTPResponse> verifyOtp(@RequestBody @Valid OTPVerificationRequest request) {
        boolean isValid = registrationService.verifyOtp(request.getEmail(), request.getOtp(), OtpEntry.ActionType.REGISTRATION);
        if (isValid) {
            return ResponseEntity.ok(new OTPResponse("OTP verified successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OTPResponse("Invalid or expired OTP"));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<RegistrationConfirmResponse> confirmRegistration(@RequestBody ConfirmRegistrationRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        String otpCheckToken = UUID.randomUUID().toString();
        return ResponseEntity.ok(new RegistrationConfirmResponse(otpCheckToken));
    }
}

