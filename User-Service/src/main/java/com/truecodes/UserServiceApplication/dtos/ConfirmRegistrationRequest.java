package com.truecodes.UserServiceApplication.dtos;

import lombok.*;

@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRegistrationRequest {
    private String email;
    private Integer password;
}
