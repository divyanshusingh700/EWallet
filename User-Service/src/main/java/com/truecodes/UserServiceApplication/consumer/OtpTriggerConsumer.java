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
    public void consume(String email) {
        String clientIp = InetAddress.getLoopbackAddress().getHostAddress(); // Ideally passed in message or context
        otpService.processOtpRequest(email, clientIp, OtpEntry.ActionType.REGISTRATION);
    }
}
