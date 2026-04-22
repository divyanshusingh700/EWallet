package com.truecodes.UserServiceApplication.dtos;

import lombok.*;

@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class RegistrationConfirmResponse {
    private String otpCheckToken;

    public RegistrationConfirmResponse(String otpCheckToken) {
        this.otpCheckToken = otpCheckToken;
    }

    public String getOtpCheckToken() {
        return otpCheckToken;
    }

    public void setOtpCheckToken(String otpCheckToken) {
        this.otpCheckToken = otpCheckToken;
    }

}
