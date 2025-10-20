package com.truecodes.UserServiceApplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truecodes.UserServiceApplication.model.LoginEntry;
import com.truecodes.UserServiceApplication.model.OtpEntry;
import com.truecodes.UserServiceApplication.model.Users;
import com.truecodes.UserServiceApplication.repository.LoginTokenRepository;
import com.truecodes.UserServiceApplication.repository.OtpEntryRepository;
import com.truecodes.UserServiceApplication.repository.UserRepository;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private LoginTokenRepository loginTokenRepository;
    @Autowired
    private OtpEntryRepository otpEntryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    @Value("${user.Authority}")
    private String userAuthority;

    @Value("${admin.Authority}")
    private String adminAuthority;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    public void updateToken(String email, String otpCheckToken, String refreshOtpCheckToken) {
        LocalDateTime now = LocalDateTime.now();

        LoginEntry loginEntry = new LoginEntry();
        loginEntry.setEmail(email);
        loginEntry.setCreatedAt(now);
        loginEntry.setUpdatedAt(now);
        loginEntry.setOtpCheckToken(otpCheckToken);
        loginEntry.setRefreshOtpCheckToken(refreshOtpCheckToken);

        loginTokenRepository.save(loginEntry);
    }
    public void processOtpRequest(String username, String clientIp, OtpEntry.ActionType actionType) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime now = LocalDateTime.now();

        OtpEntry otpEntry = new OtpEntry();
        if(username.matches(".*\\d.*")){
            otpEntry.setContact(username);
        }else{
            otpEntry.setEmail(username);
        }
        otpEntry.setOtp(otp);
        otpEntry.setClientIp(clientIp);
        otpEntry.setActionType(actionType);
        otpEntry.setAttempts(0);
        otpEntry.setCreatedAt(now);
        otpEntry.setUpdatedAt(now);
        otpEntry.setExpiresAt(now.plusMinutes(10));
        otpEntry.setExpiryTime(600);

        otpEntryRepository.save(otpEntry);
        // (plug in an email service here) using notification service
    }

    public boolean verifyOtp(String contact, String otp, OtpEntry.ActionType actionType) {
        Optional<OtpEntry> optionalEntry = otpEntryRepository.findTopByContactAndActionTypeOrderByCreatedAtDesc(contact, actionType);
        if (optionalEntry.isEmpty()) return false;
        Users optionalUserFromContact = userRepository.findByContact(contact);

        if(optionalUserFromContact!=null){
            throw new RuntimeException("User already exists");
        }
        OtpEntry entry = optionalEntry.get();
        if (entry.getExpiresAt().isBefore(LocalDateTime.now())) return false;

        if (entry.getOtp().equals(otp)) return true;
        else {
            entry.setAttempts(entry.getAttempts() + 1);
            otpEntryRepository.save(entry);
            return false;
        }
    }

    public Users addUserInDB(String contact, String password) throws JsonProcessingException {
        logger.info("we came here to add this new user into db");

        Users user = new Users(contact);

        user.setAuthorities(userAuthority);
        user.setPassword(passwordEncoder.encode(password));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("contact", contact);
         user = userRepository.save(user);

        kafkaTemplate.send("USER_CREATED_FROM_CONSOLE", objectMapper.writeValueAsString(jsonObject));
        return user;
    }

}
