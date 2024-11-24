package com.truecodes.UserServiceApplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.truecodes.UserServiceApplication.dtos.UserRequestDTO;
import com.truecodes.UserServiceApplication.model.Users;
import com.truecodes.UserServiceApplication.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/addUpdate")
    private ResponseEntity<Users> addUpdate(@RequestBody @Valid UserRequestDTO dto) throws JsonProcessingException {
        Users user = userService.addUpdate(dto);

        if(user != null){
            return new ResponseEntity(user, HttpStatus.OK);
        }
        return new ResponseEntity(user, HttpStatus.BAD_REQUEST);
    }
}
