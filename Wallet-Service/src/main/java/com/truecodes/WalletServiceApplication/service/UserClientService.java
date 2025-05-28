package com.truecodes.WalletServiceApplication.service;

import com.truecodes.WalletServiceApplication.exceptionHandler.ClientSideAPIRequestException;
import com.truecodes.utilities.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.factory.annotation.Value;

@Service
public class UserClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${userservice.base-url}")
    private String userServiceBaseUrl;

    public UserDTO getUserNameById(String userId) {
        String url = userServiceBaseUrl + "/user/" + userId;
        try {
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (HttpClientErrorException e) {
            throw new ClientSideAPIRequestException("User not found", HttpStatus.NOT_FOUND);
        }
    }
}

