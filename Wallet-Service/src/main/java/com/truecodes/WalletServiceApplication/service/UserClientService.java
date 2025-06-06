package com.truecodes.WalletServiceApplication.service;

import com.truecodes.WalletServiceApplication.controller.WalletController;
import com.truecodes.WalletServiceApplication.exceptionHandler.ClientSideAPIRequestException;
import com.truecodes.utilities.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

@Service
public class UserClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${userservice.base-url}")
    private String userServiceBaseUrl;
    private static Logger logger = LoggerFactory.getLogger(WalletController.class);

    public UserDTO getUserNameById(String userId, String token) {
        String url = userServiceBaseUrl + "/user/" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
//            return restTemplate.getForObject(url, UserDTO.class);
            return restTemplate.exchange(url, HttpMethod.GET, entity, UserDTO.class).getBody();
        } catch (HttpClientErrorException e) {
            logger.info(Arrays.toString(e.getStackTrace()));
            throw new ClientSideAPIRequestException("User not found", HttpStatus.NOT_FOUND);
        }
    }
}

