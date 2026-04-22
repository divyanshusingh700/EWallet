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
public class ConfirmRegistrationRequest {
    @NotBlank(message = "Contact is mandatory")
    private String username;
    @NotNull
    private String password;
}
