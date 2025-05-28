package com.truecodes.UserServiceApplication.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateRequest {
    private String contact;
    private String password;
}
