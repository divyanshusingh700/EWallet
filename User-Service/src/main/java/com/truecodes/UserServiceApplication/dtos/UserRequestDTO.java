package com.truecodes.UserServiceApplication.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truecodes.utilities.UserIdentifier;
import com.truecodes.UserServiceApplication.model.UserType;
import com.truecodes.UserServiceApplication.model.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
//@RequiredArgsConstructor
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String name;
    @NotBlank(message = "contact can not be blank")
    private String contact;
    @NotBlank(message = "email can not be blank")
    private String email;
    private String address;
    private String dob;
    @NotBlank(message = "password can not be blank")
    private String password;

    @NotNull(message = "userIdentifier can not be null")
    private UserIdentifier userIdentifier;
    @NotBlank(message = "userIdentifierValue can not be blank")
    private String userIdentifierValue;

    public Users toUser() {
        return Users.builder()
                .name(this.name)
                .contact(this.contact)
                .email(this.email)
                .address(this.address)
                .dob(this.dob)
                .userIdentifierValue(this.userIdentifierValue)
                .identifier(this.userIdentifier)
                .enabled(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .userId(UUID.randomUUID().toString())
                .userType(UserType.USER)
                .build();
    }
}
