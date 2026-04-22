package com.truecodes.UserServiceApplication.dtos;

import lombok.*;

@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private int expiresIn;
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private String error;
    public AuthResponse(String error){
        this.error = error;
    }

    public AuthResponse(int expiresIn, String bearer, String accessToken, String refreshToken) {
        this.expiresIn = expiresIn;
        this.tokenType = bearer;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
