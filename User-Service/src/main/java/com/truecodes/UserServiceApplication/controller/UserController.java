package com.truecodes.UserServiceApplication.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.truecodes.UserServiceApplication.dtos.UserRequestDTO;
import com.truecodes.UserServiceApplication.model.AuthenticateRequest;
import com.truecodes.UserServiceApplication.model.AuthenticateResponse;
import com.truecodes.UserServiceApplication.model.Users;
import com.truecodes.UserServiceApplication.service.UserService;
import com.truecodes.utilities.auth.JwtUtil;
import com.truecodes.utilities.dto.UserDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticateRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getContact(), authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(authRequest.getContact());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticateResponse(jwt));
    }

    @PostMapping("/addUpdate")
    private ResponseEntity<Users> addUpdate(@RequestBody @Valid UserRequestDTO dto) throws JsonProcessingException {
        Users user = userService.addUpdate(dto);
        logger.info("we came here in add update controller after service class method");
        if(user != null){
            logger.info("user != null");

            return new ResponseEntity(user, HttpStatus.OK);
        }
        return new ResponseEntity(user, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    private ResponseEntity<Users> register(@RequestBody @Valid UserRequestDTO dto) throws JsonProcessingException {
        Users user = userService.addUpdate(dto);
        logger.info("we came here in add update controller after service class method");
        if(user != null){
            logger.info("user != null");

            return new ResponseEntity(user, HttpStatus.OK);
        }
        return new ResponseEntity(user, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/userDetails")
    public Users getUserDetails(@RequestParam("contact") String contact){
        System.out.println("came in userDetails");
        Users u = (Users) userService.loadUserByUsername(contact);
        return u;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        UserDTO user = userService.findUserById(userId);
        return ResponseEntity.ok(user);
    }
}
