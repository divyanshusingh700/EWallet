package com.truecodes.WalletServiceApplication.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truecodes.WalletServiceApplication.model.CurrencyType;
import com.truecodes.utilities.CommonConstants;
import com.truecodes.WalletServiceApplication.model.Wallet;
import com.truecodes.WalletServiceApplication.repository.WalletRepository;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserCreatedConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${user.creation.time.balance}")
    private double balance;

    private static Logger logger = LoggerFactory.getLogger(UserCreatedConsumer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private WalletRepository walletRepository;

    @KafkaListener(topics = CommonConstants.USER_CREATED_TOPIC, groupId = "wallet-group")
    public void createWallet(String msg) throws JsonProcessingException {
        JSONObject jsonObject = objectMapper.readValue(msg, JSONObject.class);
        String userId = (String) jsonObject.get(CommonConstants.USER_ID);
        String contact = (String) jsonObject.get(CommonConstants.USER_CONTACT);

        long randomNumber = Math.abs(new Random().nextLong() % 1_000_000_000_000L);
        String walletSerial = String.format("%012d", randomNumber);

        Wallet wallet = Wallet.builder().
                contact(contact).
                userId(userId).
                isBlocked(false).
                active(true).
                walletSerial(walletSerial).
                currency(CurrencyType.INR).
                totalAmount(balance).
                build();

        walletRepository.save(wallet);

        logger.info("wallet has been created for the user");
        JSONObject object = new JSONObject();
        object.put(CommonConstants.USER_ID, userId);
        object.put(CommonConstants.WALLET_BALANCE, balance);
        kafkaTemplate.send(CommonConstants.WALLET_CREATED_TOPIC, objectMapper.writeValueAsString(object));

        logger.info("produced the wallet creating message in the queue for user id" + userId);
    }
}
