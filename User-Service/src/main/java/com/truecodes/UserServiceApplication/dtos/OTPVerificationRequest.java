package com.truecodes.UserServiceApplication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OTPVerificationRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;
}
