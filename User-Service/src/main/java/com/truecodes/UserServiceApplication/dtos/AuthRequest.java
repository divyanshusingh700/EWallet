package com.truecodes.UserServiceApplication.dtos;

import lombok.*;

@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String otpCheckToken;
    private String refreshOtpCheckToken;
    private String password;
    private String username;

}
