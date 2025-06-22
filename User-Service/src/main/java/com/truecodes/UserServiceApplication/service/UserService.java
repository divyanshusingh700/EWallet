package com.truecodes.UserServiceApplication.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.truecodes.UserServiceApplication.dtos.UserRequestDTO;
import com.truecodes.UserServiceApplication.exceptionHandler.ClientSideAPIRequestException;
import com.truecodes.UserServiceApplication.model.Users;
import com.truecodes.UserServiceApplication.repository.UserRepository;
import com.truecodes.utilities.CommonConstants;
import com.truecodes.utilities.UserIdentifier;
import com.truecodes.utilities.dto.UserDTO;
import jakarta.validation.Valid;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    public Users addKYCDetails(@Valid UserRequestDTO dto) throws JsonProcessingException {
        // check if user is present in db
        logger.info("we came here in add update service class method");
        // need to extract the email and password from
        Users user = dto.toUser();
        user.setAuthorities(userAuthority);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        String contact = user.getContact();
        String idVal = user.getUserIdentifierValue();
        UserIdentifier identifier = user.getIdentifier();
        String email = user.getEmail();
        if (isContactExists(contact)) {
            throw new ClientSideAPIRequestException("Contact already exists", HttpStatus.BAD_REQUEST);
        }
        if (isIdExists(idVal)) {
            throw new ClientSideAPIRequestException("User Identifier Value should be unique", HttpStatus.BAD_REQUEST);
        }
        if (isEmailExists(email)) {
            throw new ClientSideAPIRequestException("Email should be unique", HttpStatus.BAD_REQUEST);
        }

        if(identifier == UserIdentifier.AADHAAR_CARD) {
            if(idVal.length()!=12){
                throw new ClientSideAPIRequestException("User Identifier value should be of length 12", HttpStatus.BAD_REQUEST);
            }
        }else{
            if(idVal.length()!=11){
                throw new ClientSideAPIRequestException("User Identifier value should be of length 11", HttpStatus.BAD_REQUEST);
            }
        }
        // Wallet service , send  mail to that user has not been crated
        // kafka
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CommonConstants.USER_CONTACT,contact);
        jsonObject.put(CommonConstants.USER_EMAIL,email);
        jsonObject.put(CommonConstants.USER_NAME,user.getName());
        jsonObject.put(CommonConstants.USER_IDENTIFIER,identifier);
        jsonObject.put(CommonConstants.USER_IDENTIFIER_VALUE,idVal);
        jsonObject.put(CommonConstants.USER_ID,user.getUserId());

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

    public UserDTO findUserById(String userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserDTO.builder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public boolean isContactExists(String contact) {
        return userRepository.existsByContact(contact);
    }
    public boolean isIdExists(String idVal) {
        return userRepository.existsByUserIdentifierValue(idVal);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

}
