package com.truecodes.UserServiceApplication.dtos;

import com.truecodes.UserServiceApplication.model.OtpEntry;
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
public class OtpRequestDTO {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Action type is required")
    private OtpEntry.ActionType actionType;
}
