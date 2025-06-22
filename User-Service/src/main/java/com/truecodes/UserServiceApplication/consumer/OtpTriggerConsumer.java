package com.truecodes.UserServiceApplication.consumer;

import com.truecodes.UserServiceApplication.model.OtpEntry;
import com.truecodes.UserServiceApplication.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class OtpTriggerConsumer {
    @Autowired
    private RegistrationService otpService;

    @KafkaListener(topics = "otp-registration", groupId = "otp-group")
    public void consumeRegistrationOtp(String email) {
        String clientIp = InetAddress.getLoopbackAddress().getHostAddress();
        otpService.processOtpRequest(email, clientIp, OtpEntry.ActionType.REGISTRATION);
    }

    @KafkaListener(topics = "otp-login", groupId = "otp-group")
    public void consumeLoginOtp(String email) {
        String clientIp = InetAddress.getLoopbackAddress().getHostAddress();
        otpService.processOtpRequest(email, clientIp, OtpEntry.ActionType.LOGIN);
    }

    @KafkaListener(topics = "login-data", groupId = "otp-group")
    public void saveCheckToken(String email, String otpCheckToken, String refreshOtpCheckToken) {
        otpService.updateToken(email, otpCheckToken, refreshOtpCheckToken);
    }
}
