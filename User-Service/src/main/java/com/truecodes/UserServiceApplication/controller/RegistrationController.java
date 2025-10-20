package com.truecodes.UserServiceApplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.truecodes.UserServiceApplication.dtos.*;
import com.truecodes.UserServiceApplication.model.OtpEntry;
import com.truecodes.UserServiceApplication.model.Users;
import com.truecodes.UserServiceApplication.service.RegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger("RegistrationController.class");
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/initiate")
    public ResponseEntity<OTPResponse> initiateRegistration(@RequestBody @Valid RegistrationInitiateRequest request, HttpServletRequest servletRequest) {
        kafkaTemplate.send("otp-registration", request.getUsername());
        return ResponseEntity.ok(new OTPResponse("Request OTP by username initiated!"));
    }

    @PostMapping("/verify")
    public ResponseEntity<OTPResponse> verifyOtp(@RequestBody @Valid OTPVerificationRequest request) {
        boolean isValid = registrationService.verifyOtp(request.getContact(), request.getOtp(), OtpEntry.ActionType.REGISTRATION);
        if (isValid) {
            return ResponseEntity.ok(new OTPResponse("OTP verified successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OTPResponse("Invalid or expired OTP"));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<RegistrationConfirmResponse> confirmRegistration(@RequestBody ConfirmRegistrationRequest request) throws JsonProcessingException {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().build();
        }
        Users user = registrationService.addUserInDB(request.getUsername(), request.getPassword());
        logger.info("we came here in add update controller after service class method");
        if(user != null){
            logger.info("user is successfully added in db");
            String otpCheckToken = UUID.randomUUID().toString();
            return ResponseEntity.ok(new RegistrationConfirmResponse(otpCheckToken));
        }
        return new ResponseEntity(user, HttpStatus.BAD_REQUEST);
    }
}

