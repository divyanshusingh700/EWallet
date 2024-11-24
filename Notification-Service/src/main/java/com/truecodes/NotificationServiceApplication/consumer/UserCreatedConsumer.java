package com.truecodes.NotificationServiceApplication.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truecodes.utilities.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.json.simple.JSONObject;

@Service
public class UserCreatedConsumer {
    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = {CommonConstants.USER_CREATED_TOPIC},groupId = "notification-group")
    public void sendNotification(String msg) throws JsonProcessingException {
        JSONObject jsonObject = objectMapper.readValue(msg,JSONObject.class);
        String name = (String) jsonObject.get(CommonConstants.USER_NAME);
        String email = (String) jsonObject.get(CommonConstants.USER_NAME);
        String userIdentifier = (String) jsonObject.get(CommonConstants.USER_IDENTIFIER);
        String userIdentifierValue = (String) jsonObject.get(CommonConstants.USER_IDENTIFIER_VALUE);

    }
}
