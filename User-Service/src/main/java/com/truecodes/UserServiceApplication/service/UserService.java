package com.truecodes.UserServiceApplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truecodes.UserServiceApplication.dtos.UserRequestDTO;
import com.truecodes.UserServiceApplication.model.Users;
import com.truecodes.UserServiceApplication.repositoriy.UserRepository;
import com.truecodes.utilities.CommonConstants;
import jakarta.validation.Valid;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Value("${user.Authority}")
    private String userAuthority;

    @Value("${admin.Authority}")
    private String adminAuthority;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Users addUpdate(@Valid UserRequestDTO dto) throws JsonProcessingException {
        // check if user is present in db

        Users user = dto.toUser();
        user.setAuthorities(userAuthority);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Wallet service , send  mail to that user has not been crated
        // kafka
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_CONTACT,user.getContact());
        jsonObject.put(CommonConstants.USER_EMAIL,user.getEmail());
        jsonObject.put(CommonConstants.USER_NAME,user.getName());
        jsonObject.put(CommonConstants.USER_IDENTIFIER,user.getIdentifier());
        jsonObject.put(CommonConstants.USER_IDENTIFIER_VALUE,user.getUserIdentifierValue());
        jsonObject.put(CommonConstants.USER_ID,user.getPk());

        logger.info("json object as to string"+jsonObject);
        logger.info("json object as to string by objectMapper.writeValueAsString(jsonObject)"+objectMapper.writeValueAsString(jsonObject));

        user = userRepository.save(user);
        kafkaTemplate.send(CommonConstants.USER_CREATED_TOPIC,objectMapper.writeValueAsString(jsonObject));
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByContact(username);
        System.out.println("got the user Details" + users);
        return users;
    }
}
