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
}
