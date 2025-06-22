package com.truecodes.UserServiceApplication.service;

import com.truecodes.UserServiceApplication.model.LoginEntry;
import com.truecodes.UserServiceApplication.model.OtpEntry;
import com.truecodes.UserServiceApplication.repository.LoginTokenRepository;
import com.truecodes.UserServiceApplication.repository.OtpEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public void processOtpRequest(String email, String clientIp, OtpEntry.ActionType actionType) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime now = LocalDateTime.now();

        OtpEntry otpEntry = new OtpEntry();
        otpEntry.setEmail(email);
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

    public boolean verifyOtp(String email, String otp, OtpEntry.ActionType actionType) {
        Optional<OtpEntry> optionalEntry = otpEntryRepository.findTopByEmailAndActionTypeOrderByCreatedAtDesc(email, actionType);
        if (optionalEntry.isEmpty()) return false;

        OtpEntry entry = optionalEntry.get();
        if (entry.getExpiresAt().isBefore(LocalDateTime.now())) return false;

        if (entry.getOtp().equals(otp)) return true;
        else {
            entry.setAttempts(entry.getAttempts() + 1);
            otpEntryRepository.save(entry);
            return false;
        }
    }

}
