package com.truecodes.UserServiceApplication.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthOtpConfirmDTO {
    @NotBlank
    private String otpCheckToken;
    @NotBlank
    private String otp;
    @NotBlank
    private String login;
}
