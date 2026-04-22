package com.truecodes.UserServiceApplication.dtos;

import jakarta.validation.constraints.Email;
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
public class AuthOtpRequestDTO {
    @Email
    @NotBlank(message = "Email is mandatory")
    private String login;
    @NotNull
    private String otpCheckToken;
}
