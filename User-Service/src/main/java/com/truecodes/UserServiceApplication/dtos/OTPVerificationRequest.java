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
    @NotBlank
    private String contact;

    @NotBlank
    private String otp;
}
